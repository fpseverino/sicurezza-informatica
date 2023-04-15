//
//  euclide.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 14/04/23.
//

import Foundation

var a: Int
var b: Int

if CommandLine.argc != 3 {
    print("Usage: euclide <number1> <number2>")
    exit(1)
}

if let arg1 = Int(CommandLine.arguments[1]) {
    a = arg1
} else {
    print("ERROR: First argument is not a number")
    exit(1)
}

if let arg2 = Int(CommandLine.arguments[2]) {
    b = arg2
} else {
    print("ERROR: Second argument is not a number")
    exit(1)
}

var r: Int
while b != 0 {
    r = a % b
    a = b
    b = r
}

print("The GCD is \(a)")
