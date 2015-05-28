#!/bin/sh
if [ "$#" -ne 1 ]; then
    echo "Usage: conv.sh <filename>"
    exit
fi

awk '{printf("\"%s\", %s\n", $1, $2);}' < "$1" > "`basename $1 .dict`.csv"
