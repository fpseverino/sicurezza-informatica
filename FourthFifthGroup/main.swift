//
//  main.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 03/05/23.
//

import Foundation

// TODO: error handling

if CommandLine.arguments.count < 4 {
    print("Usage: main <miller-rabin|ctr> [number|numbers] [iterations|numbers]")
    exit(1)
}
let method = CommandLine.arguments[1]
if method == "miller-rabin" {
    let iterations = Int(CommandLine.arguments[3])!
    let number = Int(CommandLine.arguments[2])!
    print(isComposite(number, iterations: iterations) ? "Composite" : "Probably prime")
} else if method == "ctr" {
    let m: [Int]
    let a: [Int]
    if (CommandLine.arguments.count - 2) % 2 == 0 {
        m = Array(CommandLine.arguments[2..<(CommandLine.arguments.count - 2) / 2 + 2]).map { Int($0)! }
        a = Array(CommandLine.arguments[(CommandLine.arguments.count - 2) / 2 + 2..<CommandLine.arguments.count]).map { Int($0)! }
    } else {
        print("ERROR: 'm' and 'a' arrays must have the same length")
        exit(1)
    }
    if let result = ctr(m: m, a: a) {
        print(result)
    } else {
        print("ERROR: The 'm' numbers are not coprime")
    }
} else {
    print("Usage: main <miller-rabin|ctr> [number|numbers] [iterations|numbers]")
}