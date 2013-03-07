#!/usr/bin/perl -w

use strict;
use Getopt::Long;
use LWP::Simple;

my $dssp_folder = '/home/proj/biocluster/praktikum/bioprakt/Data/DSSP/';
my $pdb_folder  = '/home/proj/biocluster/praktikum/bioprakt/Data/PDB/';

my $pdb      = '';
my $dssp     = '';
my $dssp_bin = 'dssp-2.0.4-linux-amd64';

my $mode      = '';
my $dssp_file = '';

my @mandatory_options = ();
my @file_protein_list = ();
my $folder            = '';
my @pdb_id_list       = ();

my %aa = qw(
  ALA    A
  CYS    C
  ASP    D
  GLU    E
  PHE    F
  GLY    G
  HIS    H
  ILE    I
  LYS    K
  LEU    L
  MET    M
  ASN    N
  PRO    P
  GLN    Q
  ARG    R
  SER    S
  THR    T
  VAL    V
  TRP    W
  TYR    Y
);

GetOptions(
    'pdb:s'      => \$pdb,
    'dssp:s'     => \$dssp,
    'dssp-bin:s' => \$dssp_bin,
    't=s{3,3}'   => \@mandatory_options
);

$mode = $mandatory_options[0];

# pdb_ids in datei einlesen
open FILELIST, "<$mandatory_options[1]"
  or die "angegebene Datei: $mandatory_options[1] nicht gefunden\.\n";
while (<FILELIST>) {
    my $tmp = $_;
    $tmp =~ s/\n//;
    push @pdb_id_list, $tmp;
}
close FILELIST;

$dssp_file = $mandatory_options[2];

if ( $mode eq 'pdb' ) {

    # do pdb mode
    my $array_ref = getPDBFiles( \@pdb_id_list );


    @file_protein_list = @{$array_ref};
    foreach my $file (@file_protein_list) {

        # start und ende positionen TODO
        my @helix_end       = ();
        my @helix_start     = ();
        my @sheet_start     = ();
        my @sheet_end       = ();
        my %sequences       = ();
        my @sequence        = '';
        my $sequences_chain = ();
        my $last_chain      = '';
        my $hash_ref        = '';
        my $helix_chain     = ();
        my $sheet_chain     = ();

        print $file;
        my $pdb_id = $file;
        $pdb_id =~ s/.*(\w{4})\.ent$/$1/;

        print $pdb_id;

        open PDBFILE, "<$file" or die "datei nicht gefunden\n";
        while ( my $line = <PDBFILE> ) {

            # ATOM
            if ( $line =~ /^ATOM.{9}CA\s+(\w{3})\s(\w)/ ) {
                push @sequence, $1;    # VAL, etc ind @sequence einfügen
                $sequences_chain .= $2;
                $last_chain = $2;
            }

            # HELIX
            if ( $line =~ /^HELIX.{14}(\w)\s*(\d+)\s{2}\w{3}\s\w\s*(\d+)/ ) {
                $helix_chain .= $1;
                push @helix_start, $2;
                push @helix_end,   $3;
            }

            # SHEET
            if ( $line =~ /^SHEET.{16}(\w)\s*(\d+)\s\s.{5}\s*(\d+)/ ) {
                $sheet_chain .= $1;
                push @sheet_start, $2;
                push @sheet_end,   $3;
            }
        }
        close PDBFILE;

#        for my $i ( 0 .. length($helix_chain)-1 ) {
#           print "HELIX:";
#           print substr($helix_chain,$i,1),"\n",$helix_start[$i],"->",$helix_end[$i],"\n";
#        }
#
#        for my $j ( 0 .. length($sheet_chain)-1 ) {
#           print "SHEET:";
#           print substr($sheet_chain,$j,1),"\n",$sheet_start[$j],"->",$sheet_end[$j],"\n";
#        }

        # umwandeln von sequence 3-strings in sequenz
        my $tmp = '';

        # umwandeln von 3letter code in 1 letter code:
        foreach my $AS (@sequence) {
            $AS = $aa{$AS};
        }
        shift(@sequence)
          ;    #TODO fragen warum muss erstes element entfernt werden:
               # warum erstes element leer?

        #   print @sequence,"\n";
        #   print $sequences_chain,"\n";
        $hash_ref = processChains( \@sequence, $sequences_chain );
        %sequences = %$hash_ref;

        $hash_ref = createStubSecChains( \%sequences );
        my %secondary_hash = %$hash_ref;

        $hash_ref = processSecChains(
            \@helix_start, \@helix_end,  $helix_chain, \@sheet_start,
            \@sheet_end,   $sheet_chain, \%secondary_hash
        );

        %secondary_hash = %{$hash_ref};

        #nun ausgabe in file:
        open PRINTFILE, ">>$dssp_file"
          or die "output geht nicht zu erstellen warum auch immer \n";
        foreach my $key ( sort keys %sequences ) {
            print $key;
            chomp($pdb_id);
            print PRINTFILE "\> $pdb_id$key\n";
            print PRINTFILE "AS $sequences{$key}\n";
            print PRINTFILE "SS $secondary_hash{$key}\n";
            print PRINTFILE "\n";
        }
        close PRINTFILE;
    }
}

# vorbereitung für ersetzen der zeichen für sheet und helix
sub createStubSecChains {
    my $hash_ref    = $_[0];
    my %origin_hash = %$hash_ref;
    my %sec_hash    = ();
    foreach my $k ( sort keys %origin_hash ) {
        for ( 0 .. length( $origin_hash{$k} ) - 1 ) {
            $sec_hash{$k} .= 'C';
        }
    }

    return \%sec_hash;
}

sub processSecChains {
    my @hstart = @{ $_[0] };
    my @hend   = @{ $_[1] };
    my $hchain = $_[2];
    my @sstart = @{ $_[3] };
    my @send   = @{ $_[4] };
    my $schain = $_[5];
    my %hash   = %{ $_[6] };

    my $tmpstr = '';

    for my $i ( 0 .. $#hstart ) {
        my $chain = substr( $hchain, $i, 1 );
        $tmpstr = $hash{$chain};

        $tmpstr = replaceChars( $tmpstr, 'H', $hstart[$i] - 1,
            $hend[$i] - $hstart[$i] );

        #$tmpstr =~ s/^(.{$hstart[$i]}).{$hend[$i]-$hstart[$i]+1}(.*)$/$1H$2/;
        #print $tmpstr, " \n";
        #for my $x ( $hstart[$i] - 1 .. $hend[$i] ) {
        #    $tmpstr = substr( $hash{$chain}, $x, 1, 'H' );
        #}
        $hash{$chain} = $tmpstr;

        #print $hash{ substr( $hchain, $i, 1 ) };
    }

    for my $i ( 0 .. $#sstart ) {
        my $chain = substr( $schain, $i, 1 );
        $tmpstr = $hash{$chain};

        $tmpstr = replaceChars( $tmpstr, 'E', $sstart[$i] - 1,
            $send[$i] - $sstart[$i] );

        #$tmpstr =~ s/^(.{$hstart[$i]}).{$hend[$i]-$hstart[$i]+1}(.*)$/$1H$2/;
        #print $tmpstr, " \n";
        #for my $x ( $hstart[$i] - 1 .. $hend[$i] ) {
        #    $tmpstr = substr( $hash{$chain}, $x, 1, 'H' );
        #}
        $hash{$chain} = $tmpstr;

        #print $hash{ substr( $hchain, $i, 1 ) };
    }

    return \%hash;
}

#replace chars einfach und nicht bekloppt
sub replaceChars {
    my $stringoutput = '';
    my $charreplace  = $_[1];
    my $index        = $_[2];
    my $nr           = $_[3];
    my @tmparray     = split //, $_[0];

    if ( length( $_[0] ) - 1 < $index + $nr ) {
        exit 1;
    }

    for my $i ( $index .. $index + $nr ) {
        $tmparray[$i] = $charreplace;
    }

    $stringoutput = join( '', @tmparray );
    return $stringoutput;

}

# nimmt ein array von sequenzen und ein string als identifier für welche chain
sub processChains {
    my @tmparray    = split( //, $_[1] );
    my @tmpsequence = @{ $_[0] };
    my %sequences   = ();

    for my $i ( 0 .. $#tmpsequence ) {
        $sequences{ $tmparray[$i] } .= $tmpsequence[$i];
    }
    return \%sequences;
}

if ( $mode eq 'dssp' ) {
    `touch $dssp_file`;
    open FILEPRINT, ">$dssp_file";
    foreach (@pdb_id_list) {
        print $_;
        my $hash_ref = processDSSP($_);

        # in file einlesen die als dssp paramter angeben ist
        my %hash          = %{$hash_ref};
        my @aa_sequences  = @{ $hash{aa_sequences} };
        my @sec_sequences = @{ $hash{sec_sequences} };
        my @id_seq        = @{ $hash{id_seq} };
        my @seq_lengths = @{$hash{seq_length}};

        for my $i ( 0 .. $#aa_sequences ) {
            if ($seq_lengths[$i] > 40)
            {
            print FILEPRINT "\> $id_seq[$i]\n";
            print FILEPRINT "AS $aa_sequences[$i]\n";
            print FILEPRINT "SS $sec_sequences[$i]\n\n";
            }
        }
    }
    close FILEPRINT;
}

sub processDSSP {
    print $_[0];
    my @pdb_id_list_tmp = ();
    push @pdb_id_list_tmp, $_[0];

    # files suchen nach pdb ids
    my $array_ref = getDSSPFiles( \@pdb_id_list_tmp );
    print @$array_ref;
    @file_protein_list = @$array_ref;

    # files einlesen
    foreach (@file_protein_list) {
        my $reference       = '';
        my $source          = '';
        my $aa_sequence     = ();
        my $sec_sequence    = ();
        my $istable         = 0;
        my $pdbid           = '';
        my @aa_sequences    = ();
        my @sec_sequences   = ();
        my $last_line       = '';
        my @id_seq          = ();
        my $seq_break_count = 0;
        my $last_seq_id     = '';
        my @sequence_length = ();

        open FILE, "<$_" or die $!;

        #do processing
        while ( my $line = <FILE> ) {

            #Referenz:
            if ( $line =~ m/^REFERENCE (.*).$/ ) {
                $reference .= $1;
            }

            #Source:
            if ( $line =~ m/^SOURCE    (.*).$/ ) {
                $source .= $1;
            }

            #Header:
            if ( $line =~ m/^HEADER    .*(\w{4,})\s+.$/ ) {
                $pdbid = $1;
            }

            #hinzufügen von aa_sequenz und sec_sequenz:
            if ( $istable && $line =~ m/^.{13}([A-Z])..([A-Z| ])/ ) {
                my $tmp_sec = '';

                if ( $2 eq 'E' || $2 eq 'B' ) {
                    $tmp_sec = 'E';
                }
                elsif ( $2 eq 'G' || $2 eq 'H' ) {
                    $tmp_sec = 'H';
                }
                else {
                    $tmp_sec = 'C';
                }
                $aa_sequence  .= $1;
                $sec_sequence .= $tmp_sec;
            }
            if ( $line =~ m/!\*/ ) {
                push @aa_sequences,  $aa_sequence;
                push @sec_sequences, $sec_sequence;
                $aa_sequence  = '';
                $sec_sequence = '';
                $last_line =~ /^.{11}(\w)/;
                $last_seq_id = $1;
            }

            #erkennung der tabelle:
            if ( $line =~ m/^\s+#/ ) {
                $istable = 1;
            }

            # pdbid einfügen
            if ( $last_line =~ m/^.{11}\s.+!\*/ ) {

                # für new chain
                $seq_break_count = 0;
                push @id_seq, "$pdbid$last_seq_id";
                push @sequence_length, $aa_sequence.length;
            }
            elsif ( $last_line =~ m/^.{11}\s.+! / ) {

                # für chainbreak
                #my $tmp_seq_break_count = '';
                #$tmp_seq_break_count = sprintf ("%02d",int($seq_break_count));
                push @id_seq, "$pdbid$last_seq_id";

                #$seq_break_count++;
            }
            $last_line = $line;
        }

        # letzte sequenz in arrays
        push @aa_sequences,  $aa_sequence;
        push @sec_sequences, $sec_sequence;
        push @sequence_length, $aa_sequence.length;
        $last_line =~ /^.{11}(\w)/;
        push @id_seq, "$pdbid$1";
        my %hash = ();
        $hash{aa_sequences}  = \@aa_sequences;
        $hash{sec_sequences} = \@sec_sequences;
        $hash{id_seq}        = \@id_seq;
        $hash{seq_length} = \@sequence_length;
        return \%hash;
    }
}

if ( $mode eq 'cmp' ) {
    `touch $dssp_file`;

    # do cmp mode
    print "pdb cmp\n";
    `extract-dssp.pl -t pdb $mandatory_options[1] pdb_result.txt`;
    `extract-dssp.pl -t dssp $mandatory_options[1] dssp_result.txt`;

    my %pdb_hash  = ();
    my %dssp_hash = ();

    open PDB, "<pdb_result.txt" or die "fehler in pdb_result\n";
    my $last_pdb_id = '';
    while (<PDB>) {
        if ( $_ =~ /^\> ?(\w{4,5})/ ) {
            $last_pdb_id = lc($1);
        }
        if ( $_ =~ /^\w\w (.*)/ ) {
            $pdb_hash{$last_pdb_id} = $1;
        }
    }

    close PDB;
    open DSSP, "<dssp_result.txt" or die "fehler in dssp_result\n";
    $last_pdb_id = '';
    while (<DSSP>) {
        if ( $_ =~ /^\> ?(\w{4,5})/ ) {
            $last_pdb_id = lc($1);
        }
        if ( $_ =~ /^\w\w (.*)/ ) {
            $dssp_hash{$last_pdb_id} = $1;
        }
    }
    close DSSP;

    my @inconsistent_pdb_ids = ();

    my $string1 = '';
    my $string2 = '';

    foreach my $k ( sort keys %pdb_hash ) {
        if ( exists $pdb_hash{$k} && exists $dssp_hash{$k} ) {
            $string1 = $pdb_hash{$k};
            $string2 = $dssp_hash{$k};
            if ( $string1 ne $string2 ) {
                push @inconsistent_pdb_ids, $k;
            }
        }
        else {
            push @inconsistent_pdb_ids, $k;
        }
        $string1 = '';
        $string2 = '';
    }

    open PRINTFILE, ">$dssp_file" or die "nicht möglich";
    foreach my $x (@inconsistent_pdb_ids) {
        print PRINTFILE $x, "\n";
    }
    close PRINTFILE;
}

if ( $pdb ne '' ) {
    print "pdb enthalten";
}

if ( $dssp ne '' ) {
    print "dssp enthalten";
}

if ( $dssp_bin ne 'dssp-2.0.4-linux-amd64' ) {
    print "dssp-bin enthalten";
}

# suchen der dateien - obsolet
sub getFiles {
    my @pdb_id_list_tmp = @{ $_[1] };
    my @file_list       = ();
    my @missing_pdbids  = ();
    my $folder          = $_[0];

    my $endung = '';
    my $anfang = '';

    if ( $_[2] ) {
        $endung = 'dssp';
    }
    else {
        $endung = 'ent';
    }

    foreach (@pdb_id_list_tmp) {
        my $currentpath = '';    # `find $folder -name $_.$endung -type f`;
        if ( $currentpath ne '' ) {
            push @file_list, $currentpath;
        }
        else {

            # runterladen
            #         my $FILE = get("http://www.rcsb.org/pdb/files/pdb$_.pdb");
            #         open (FH,">pdb-download/pdb$_.pdb");
            #         print FH $FILE;
            downloadPDB($_);

      # konvertieren
      #         `$dssp_bin -i pdb-download/pdb$_.pdb -o dssp-converted/$_.dssp`;
      #         push @file_list,"dssp-converted/$_.dssp";
      #         close (FH);
            convertPDBtoDSSP( "pdb-download/pdb$_.pdb", $_ );
        }
    }
    return \@file_list;
}

sub getPDBFiles {
    my @pdb_id_list_tmp = @{ $_[0] };
    my @file_list       = ();

    foreach (@pdb_id_list_tmp) {
        my $currentpath = `find $pdb_folder -name pdb$_.ent -type f`;
        if ( $currentpath ne '' ) {
            push @file_list, $currentpath;
        }
        else {

            # runterladen
            downloadPDB($_);
            #my $filename = $_;
            #$filename =~ s/pdb(\w+)\.pdb/$1/;
            #convertPDBtoDSSP($_,"dssp-converted/$filename.dssp");
            push @file_list, "pdb-download/pdb$_.pdb";
            #push @file_list, "dssp-converted/$filename.dssp";
        }
    }
    return \@file_list;
}

sub getDSSPFiles {
    my @pdb_id_list_tmp = @{ $_[0] };
    my @file_list       = ();

    foreach (@pdb_id_list_tmp) {
        my $currentpath = `find $dssp_folder -name $_.dssp -type f`;
        if ( $currentpath ne '' ) {
            push @file_list, $currentpath;
        }
        else {

            # wenn keine dssp gefunden
            my $tmppath = `find $pdb_folder -name pdb$_.pdb -type f`;
            if ( $tmppath ne '' ) {
                convertPDBtoDSSP( $tmppath, $_ );
                push @file_list, "dssp-converted/$_.dssp";
            }
            else

              # wenn keine pdb gefunden
            {
                downloadPDB($_);
                convertPDBtoDSSP( "pdb-download/pdb$_.pdb", $_ );
                push @file_list, "dssp-converted/$_.dssp";
            }
        }
    }
    return \@file_list;
}

# runterladen
# arguments =
# 1 => pdb-id
sub downloadPDB {
    my $FILE = get("http://www.rcsb.org/pdb/files/pdb$_[0].pdb");
    open( FH, ">pdb-download/pdb$_[0].pdb" );
    print FH $FILE;
    close FH;
}

# dssp benutzen
# arguments =
# 1 => inpute-file pdb
# 2 => pdb-id
sub convertPDBtoDSSP {
    `$dssp_bin -i $_[0] -o dssp-converted/$_[1].dssp`;
}
