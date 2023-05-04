//
//  main.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 03/05/23.
//

import Foundation

if CommandLine.arguments.count < 4 {
    print("Usage: main <miller-rabin|crt|dh> [number|m|q] [iterations|a]")
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
} else if method == "dh" {
    let q = Int(CommandLine.arguments[2])!
    let a = Int(CommandLine.arguments[3])!
    do {
        let alice = try DH(q: q, a: a)
        let bob = try DH(q: q, a: a)
        alice.setOtherPublicKey(bob.getMyPublicKey())
        bob.setOtherPublicKey(alice.getMyPublicKey())
        print(alice.getSecretSharedKey() == bob.getSecretSharedKey() ? "OK" : "KO")
        print("Alice's public key: \(alice.getMyPublicKey())")
        print("Bob's public key: \(bob.getMyPublicKey())")
        print("Alice's secret shared key: \(alice.getSecretSharedKey()!)")
        print("Bob's secret shared key: \(bob.getSecretSharedKey()!)")
    } catch DHError.notPrime {
        print("ERROR: 'q' must be prime")
        exit(1)
    } catch DHError.notPrimitiveRoot {
        print("ERROR: 'a' must be a primitive root of 'q'")
        exit(1)
    } catch {
        print("ERROR: Unknown error")
        exit(1)
    }
} else {
    print("Usage: main <miller-rabin|crt|dh> [number|m|q] [iterations|a]")
}