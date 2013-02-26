#!/usr/bin/perl
use Getopt::Std;

my %opts;
getopts( 'n:l:o:', \%opts );

$limit   = $opts{l};
$counter = 0;
$nr = $opts{n};

open FILE, ">$opts{o}" or die "klappt nicht mit dem erstellen $!\n";
opendir( DIR, "$opts{i}\/" ) or die "$opts{i}\/\n";
while ( my $f = readdir(DIR) ) {    # homstrad ordner
    if ( $f !~ m/^\./ && -d "$opts{i}\/$f\/" ) {

        # werden versteckte (.dateiname) dateien nicht aufgerufen
        opendir( SUBDIR, "$opts{i}\/$f\/" )
          or die "ordner nicht vorhandenunterordner:$f\n";
        while ( my $sf = readdir(SUBDIR) ) {    #unterodern = domäne
            if ( $sf =~ m/^(.{3,5}\.dssp$/ ) {
                if ( $counter < $limit && int(rand($nr)) >= 10) {
                    print FILE "$sf";
                    $counter++;
                }
            }
        }
        closedir(SUBDIR);
    }
}
closedir(DIR);
close FILE;
