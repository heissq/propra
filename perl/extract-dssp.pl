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
}

if ( $mode eq 'dssp' ) {

   # do dssp mode

   # files suchen nach pdb ids
   my $array_ref = getFiles( $dssp_folder, \@pdb_id_list, 1 );
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

if ( $dssp_bin ne '' ) {
   print "dssp-bin enthalten";
}

# suchen der dateien
sub getFiles {
   my @pdb_id_list_tmp = @{ $_[1] };
   my @file_list       = ();
   my @missing_pdbids  = ();
   my $folder = $_[0];

   # TODO filedownload when no file for pdbid
   # -aufgabenblatt 3 aufgabe 10
   my $endung = '';

   if ( $_[2] ) {
      $endung = 'dssp';
   }
   else {
      $endung = 'ent';
   }

   foreach (@pdb_id_list_tmp) {
      my $currentpath = ''; # `find $folder -name $_.$endung -type f`;
      if ( $currentpath ne '' ) {
         push @file_list, $currentpath;
         print "gefunden und $currentpath";
      }
      else {
         # runterladen
         my $FILE = get("http://www.rcsb.org/pdb/files/pdb$_.pdb");
         open (FH,">pdb-download/pdb$_.pdb");
         print FH $FILE;

         # konvertieren
         `$dssp_bin -i pdb-download/pdb$_.pdb -o dssp-converted/$_.dssp`; 
         push @file_list,"dssp-converted/$_.dssp";
         close (FH);
      }
   }

   opendir( DIR, "$_[0]" ) or die "$_[0]\/\n";
   while ( my $f = readdir(DIR) ) {    # dssp- oder pdb-ordner
      if ( $f !~ m/^\./ && -d "$_[0]\/$f\/" ) {

         # werden versteckte (.dateiname) dateien nicht aufgerufen
         opendir( SUBDIR, "$_[0]\/$f\/" )
            or die "ordner nicht vorhanden unterordner:$f\n";
         while ( my $sf = readdir(SUBDIR) ) {    # unterordner
            if ( $_[0] =~ m/DSSP/ ) {

               # dssp mode
               for (@pdb_id_list_tmp) {
                  if ( $sf =~ m/(\w{4,})\.dssp$/ ) {
                     if ( $1 eq $_ ) {
                        push @file_list, "$_[0]\/$f\/$sf";
                     }
                  }
               }
            }
            else {
               # pdb mode
               for (@file_list) {
                  if ( $sf =~ m/^pdb(.{4,})\.ent$/ ) {    #
                     if ( $1 eq $_ ) {
                        push @file_list, "$_[0]\/$f\/$sf";
                     }
                  }
               }

            }
         }
         closedir(SUBDIR);
      }
   }
   closedir(DIR);

   return \@file_list;
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
