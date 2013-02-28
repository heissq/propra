#!/usr/bin/perl -w

$string  = 'daaa';

for $i ( 0 .. length($string) ) {
  print substr($string,$i,1),"\n";
}

$string2
