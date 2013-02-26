#!/usr/bin/perl -w

use strict;
use Getopt::Long;

my $dssp_folder = '/home/proj/biocluster/praktikum/bioprakt/Data/DSSP/';
my $pdb_folder  = '/home/proj/biocluster/praktikum/bioprakt/Data/PDB/';

my $pdb      = '';
my $dssp     = '';
my $dssp_bin = '';

my $mode      = '';
my $dssp_file = '';
my $isdssp    = '';

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

my $some_string = "string mit newline\n macht eine newline";

$dssp_file = $mandatory_options[2];

if ( $mode eq 'pdb' ) {

   # do pdb mode
   print "pdb mode\n";
}

if ( $mode eq 'dssp' ) {
   # do dssp mode

   # files suchen nach pdb ids
   $isdssp = 1;
   my $array_ref = getFiles($dssp_folder);
   @file_protein_list = @{$array_ref};

   # files einlesen
   print "##########\nDSSP-Modus:\n##########\n\n\n";
   foreach (@file_protein_list) {
      my $reference    = '';
      my $source       = '';
      my $aa_sequence  = ();
      my $sec_sequence = ();
      my $istable      = 0;
      my $pdbid        = '';

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
         if ( $line =~ m/^HEADER    .*(\w{3,4})\s+.$/ ) {
            $pdbid .= $1;
         }

         #erkennung der tabelle:
         if ( $line =~ m/^\s+#\s+RESIDUE/ ) {
            $istable = 1;
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
      }

      close FILE;
      print "Referenz:\n$reference\n";
      print "PDB-Id:\n$pdbid\n";
      print
      "Quelle:\n$source\n\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\n";
      print "  $aa_sequence\n";
      print "  $sec_sequence\n";
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
   my @file_list = ();
   opendir( DIR, "$_[0]" ) or die "$folder\/\n";
   while ( my $f = readdir(DIR) ) {    # dssp- oder pdb-ordner
      print "$f\+";
      if ( $f !~ m/^\./ && -d "$folder\/$f\/" ) {
         # werden versteckte (.dateiname) dateien nicht aufgerufen
         opendir( SUBDIR, "$_[0]\/$f\/" )
            or die "ordner nicht vorhanden unterordner:$f\n";
         while ( my $sf = readdir(SUBDIR) ) {    # unterodnern
            print "$_[0]\/$f";
            if ($isdssp) {

               # dssp mode
               for (@file_list) {
                  if ( $sf =~ m/^(.{3,5}).*\.dssp$/ ) {
                     if ( $1 eq $_ ) {
                        push @file_list,$sf;
                     }
                  }
               }
            }
            else {
               # pdb mode
               for (@file_list) {
                  if ( $sf =~ m/^pdb(.{3,5}).*\.ent$/ ) {
                     if ( $1 eq $_ ) {
                        push @file_list,$sf;
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
