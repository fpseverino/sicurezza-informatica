//
//  main.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 03/05/23.
//

import Foundation

let method = CommandLine.arguments[1]
if method == "crt" {
    if CommandLine.arguments.count < 6 || CommandLine.arguments.count % 2 != 0 {
        print("ERROR: wrong number of arguments")
        print("Usage: main <crt|dh|rsa> [m|q|p] [a|a|q] [e] <message>")
        exit(1)
    }
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
    if CommandLine.arguments.count != 4 {
        print("ERROR: wrong number of arguments")
        print("Usage: main <crt|dh|rsa> [m|q|p] [a|a|q] [e] <message>")
        exit(1)
    }
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
} else if method == "rsa" {
    if CommandLine.arguments.count != 6 {
        print("ERROR: wrong number of arguments")
        print("Usage: main <crt|dh|rsa> [m|q|p] [a|a|q] [e] <message>")
        exit(1)
    }
    let p = Int(CommandLine.arguments[2])!
    let q = Int(CommandLine.arguments[3])!
    let e = Int(CommandLine.arguments[4])!
    let message = Int(CommandLine.arguments[5])!
    do {
        let alice = try RSA(p: p, q: q, e: e)
        let bob = try RSA(p: p, q: q, e: e)
        let encryptedMessage = try alice.encrypt(message)
        let decryptedMessage = bob.decrypt(encryptedMessage)
        print("Message: \(message)")
        print("Encrypted message: \(encryptedMessage)")
        print("Decrypted message: \(decryptedMessage)")
    } catch RSAError.notPrime {
        print("ERROR: 'p' and 'q' must be prime")
        exit(1)
    } catch RSAError.samePrimes {
        print("ERROR: 'p' and 'q' must be different")
        exit(1)
    } catch RSAError.notCoprime {
        print("ERROR: 'e' and '(p - 1) * (q - 1)' must be coprime")
        exit(1)
    } catch RSAError.eTooBig {
        print("ERROR: 'e' must be less than '(p - 1) * (q - 1)'")
        exit(1)
    } catch RSAError.noInverse {
        print("ERROR: 'e' must have an inverse modulo '(p - 1) * (q - 1)'")
        exit(1)
    } catch RSAError.mTooBig {
        print("ERROR: 'm' must be less than 'n'")
        exit(1)
    } catch {
        print("ERROR: Unknown error")
        exit(1)
    }
} else {
    print("Usage: main <crt|dh|rsa> [m|q|p] [a|a|q] [e] <message>")
}