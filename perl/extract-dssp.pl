#!/usr/bin/perl -w

use strict;
use Getopt::Long;

my $pdb      = '';
my $dssp     = '';
my $dssp_bin = '';

my $mode              = '';
my $file_protein_list = '';
my $dssp_file         = '';

my @mandatory_options = ();

GetOptions(
    'pdb:s'      => \$pdb,
    'dssp:s'     => \$dssp,
    'dssp-bin:s' => \$dssp_bin,
    't=s{3,3}'   => \@mandatory_options
);

print "split(', ',@mandatory_options\n";

if ($pdb ne '') {
  print "pdb enthalten";
}

if ($dssp ne '') {
   print "dssp enthalten";
}

if ($dssp_bin ne '') {
   print "dssp-bin enthalten";
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
