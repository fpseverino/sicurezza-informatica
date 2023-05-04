//
//  main.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 03/05/23.
//

import Foundation

if CommandLine.arguments.count < 4 {
    print("Usage: main <miller-rabin|crt> [number|numbers] [iterations|numbers]")
    exit(1)
}
let method = CommandLine.arguments[1]
if method == "miller-rabin" {
    let iterations = Int(CommandLine.arguments[3])!
    let number = Int(CommandLine.arguments[2])!
    print(isComposite(number, iterations: iterations) ? "Composite" : "Probably prime")
} else if method == "crt" {
    let m: [Int]
    let a: [Int]
    m = Array(CommandLine.arguments[2..<(CommandLine.arguments.count - 2) / 2 + 2]).map { Int($0)! }
    a = Array(CommandLine.arguments[(CommandLine.arguments.count - 2) / 2 + 2..<CommandLine.arguments.count]).map { Int($0)! }
    do {
        print(try crt(m: m, a: a))
    } catch CRTError.notCoprime {
        print("ERROR: 'm' array must contain coprime numbers")
        exit(1)
    } catch CRTError.notSameLength {
        print("ERROR: 'm' and 'a' arrays must have the same length")
        exit(1)
    } catch {
        print("ERROR: unknown error")
        exit(1)
    }
} else {
    print("Usage: main <miller-rabin|crt> [number|numbers] [iterations|numbers]")
}