//
//  euclide.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 14/04/23.
//

import Foundation

var a = Int(CommandLine.arguments[1]) ?? 0
var b = Int(CommandLine.arguments[2]) ?? 0

var r: Int
while b != 0 {
    r = a % b
    a = b
    b = r
}

print("The GCD is \(a)")
