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
ALA     A
CYS     C
ASP     D
GLU     E
PHE     F
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
    print "pdb mode\n";
    my $array_ref = getPDBFiles( \@pdb_id_list );

@file_protein_list = @{$array_ref};
foreach (@file_protein_list) {
    print $_;

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

    open PDBFILE, "<$_" or die "datei nicht gefunden\n";
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

foreach my $key ( sort keys %sequences ) {
    print "$key => $sequences{$key}\n";
}

print "\n\n\n";
$hash_ref = createStubSecChains( \%sequences );
        my %secondary_hash = %$hash_ref;
        foreach my $key ( sort keys %secondary_hash ) {
            print "$key => $secondary_hash{$key}\n";
        }

        processSecChains(
            \@helix_start, \@helix_end,  $helix_chain, \@sheet_start,
\@sheet_end,   $sheet_chain, \%secondary_hash
        );
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
    print join( ',', @hstart );
    my @hend = @{ $_[1] };
    print "\n";
    print join( ',', @hend );
    print "\n";
    my $hchain = $_[2];
    my @sstart = @{ $_[3] };
    my @send   = @{ $_[4] };
    my $schain = $_[5];
    my %hash   = %{ $_[6] };

    for my $i ( 0 .. $#hstart ) {
        my $chain = substr( $hchain, $i, 1 );
        print "meine chain id : ",$chain,"\n";
        print "meine helixstartindex : $i\n";
        my $tmpstr = $hash{$chain};

        print $tmpstr, "\n";
        $tmpstr = replaceChars($tmpstr,'H',$hstart[$i],$hend[$i]-$hstart[$i]+1);
        #$tmpstr =~ s/^(.{$hstart[$i]}).{$hend[$i]-$hstart[$i]+1}(.*)$/$1H$2/;
        print $tmpstr, " \n";
        for my $x ( $hstart[$i] - 1 .. $hend[$i] ) {
            $tmpstr = substr( $hash{$chain}, $x, 1, 'H' );
        }
        $hash{ substr( $hchain, $i, 1 ) } = $tmpstr;

        #print $hash{ substr( $hchain, $i, 1 ) };
    }
    foreach my $key ( sort keys %hash ) {
        print "$key => $hash{$key}\n";
        print "$key => ";
        for (0 .. $hstart[0]-1 ) {
            print "C";
        }
        print "\n";
    }
}

sub replaceChars {
    my $stringoutput = '';
    my $charreplace = $_[1];
    my $index = $_[2];
    my $nr = $_[3];
    my @tmparray = split //,$_[0];

    if (length($_[0])-1 < $index+$nr) {
        exit 1;
    }

    for my $i ( $index .. $index+$nr) {
        $tmparray[$i] = $charreplace;
    }

    $stringoutput = join ('',@tmparray);

    print $stringoutput;
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

    # do dssp mode

    # files suchen nach pdb ids
    my $array_ref = getDSSPFiles( \@pdb_id_list );
@file_protein_list = @{$array_ref};

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

    print $_;
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
    $last_line =~ /^.{11}(\w)/;
    push @id_seq, "$pdbid$1";

    # in file einlesen die als dssp paramter angeben ist
    open FILEPRINT, ">$dssp_file"
        or die "kann datei $dssp_file nicht erstellen\n";
    for my $i ( 0 .. $#aa_sequences ) {
        print FILEPRINT "\> $id_seq[$i]\n";
        print FILEPRINT "AS $aa_sequences[$i]\n";
        print FILEPRINT "SS $sec_sequences[$i]\n\n";
    }
    close FILEPRINT;
}
}

if ( $mode eq 'cmp' ) {

    # do cmp mode
    print "pdb cmp\n";
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
            push @file_list, "pdb-download/pdb$_.pdb";
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

=begin GHOSTCODE
opendir(DIR,$opts{d}) or die ": ordner nicht gefunden $opts{n}\n";


while (my $f = readdir(DIR)) { # homstrad ordner
  if ($f !~ m/^\./ ) { 
    # werden versteckte (.dateiname) dateien nicht aufgerufen
    opendir(SUBDIR,"$opts{d}\/$f\/") or die "ordner nicht vorhanden: $!\n";
    while (my $sf = readdir(SUBDIR)) { #unterodern = domäne

# hier datei.t#em parsen mit hash rückgabe
      if ( $sf =~ m/.tem$/ ) {
        open FILE, "<$opts{d}\/$f\/$sf" or die $!;
        # ids
        my @ids = ();
        # sequenz
        my $sequence = '';
        my @sequences = ();
        # für typ suche
        my $lastlinecount = 0;
        my $isalignment = 0;
        my $issec= 0;
        my $isdssp= 0;
        my @dssp = ();
        my @alignment = ()
        my @secstructure = ();
        my $idsread = 0;
        my $changed = 0;

        while (<FILE>) {
          # type extract
          if ( $lastlinecount == 1) {
            my $tmp = $_;
            $tmp =~ s/\n//;
            if ( $tmp =~ m/^sequence$/ ) {
              $isalignment = 1;
            } else {
              $idsread = 1;
            }
            if ( $tmp =~ m/^secondary structure and phi angle$/ ) {
              $issec = 1;
            }
            if ( $tmp =~ m/^DSSP$/ )
            {
              $isdssp = 1;
            }
            $lastlinecount = 2;
          }

          # id extract
          if ( $_ =~ m/^\>P1\;([^\s]*)/ )
          {
            #print $;
            if (!$changed && $idsread ) {
              $changed = 1;
              pop(@ids);
            }
            if ( !$idsread ) {
              push(@ids,$1);
            }
          $lastlinecount = 1;
          }



        # sequenz extract
        if ( $_ =~ m/^([A-Z0-9\-]+)\*?/ && $lastlinecount == 2 )
        {
          my $tmp = $1;
          $sequence .= $tmp;
          if ( $_ =~ m/\*$/ ) {
            if ( $isalignment ) {
              push(@alignment,$sequence);
              my $rawseq = $sequence;
              $rawseq =~ s/\-//g;
              push(@sequences,$rawseq);
              $isalignment = 0;
            }
            if ( $issec ) {
              push(@secstructure,$sequence);
              $issec = 0;
            }
            if ( $isdssp ) {
              $sequence =~ s/^DSSP//;
              push(@dssp,$sequence);
              $isdssp = 0;
            }
            $sequence = '';
            $lastlinecount = 0,
          }
        }
      }

      for my $someval (0 .. $#ids) {
#          print "id\: $ids[$someval]\n";
#          print "sequence\: $sequences[$someval]\n";
#          print "alignment\: $alignment[$someval]\n";
#          print "secondary structure: $secstructure[$someval]\n";
#          print "dssp\: $dssp[$someval]\n";
#          print "file\: $sf\n";
#          print "\n\n";
if ( $dssp[0] ) {
   if ( $dssp[$someval] =~ m/^DSSP/ ) {
      print $sf;
   }
}
      }


      close(FILE); #jetzt in datenbank hinzufügen
      #exit 1;

      my $align_id = -1;
      for my $i ( 0 .. $#ids ) {
         my $foundid = getIdFromSeq($sequences[$i]);
         if ($foundid == -1) {
            #print $ids[$i];
            if ( exists $sequences[$i] ) {
               insertSeq($sequences[$i],$dbsource,$ids[$i]);
               my $lastinsertid = getIdFromSeq($sequences[$i]);
               if ($align_id == -1)
               {
                  $align_id = $lastinsertid;
               }
               insertSecondaryStrucures($secstructure[$i],$lastinsertid,'secondary structure and phi angle');
               if (exists $dssp[$i]) {
                  insertSecondaryStrucures($dssp[$i],$lastinsertid,'DSSP');
               }
               insertMultipleAlignment($alignment[$i],$lastinsertid,$align_id);
            } 
         }
      }
   }
   $counter++;
   if ( $counter == 100) {
      #exit 1;
   }
}
closedir(SUBDIR);
}
}
closedir(DIR);
$dbh->disconnect();
end=GHOSTCODE

=cut

## Please see file perltidy.ERR
