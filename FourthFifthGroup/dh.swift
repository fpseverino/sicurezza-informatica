//
//  dh.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 04/05/23.
//

import Foundation

class DH {
    let q: Int
    let a: Int
    let privateKey: Int
    let myPublicKey: Int

    var otherPublicKey: Int?
    var secretSharedKey: Int?

    init(q: Int, a: Int) throws {
        if isComposite(q, iterations: 10) {
            throw DHError.notPrime
        }
        if !isPrimitiveRoot(a, modulo: q) {
            throw DHError.notPrimitiveRoot
        }
        self.q = q
        self.a = a
        privateKey = Int.random(in: 0..<q)
        myPublicKey = power(a, privateKey, modulo: q)
    }

    func setOtherPublicKey(_ otherPublicKey: Int) {
        self.otherPublicKey = otherPublicKey
        secretSharedKey = power(otherPublicKey, privateKey, modulo: q)
    }

    func getMyPublicKey() -> Int {
        return myPublicKey
    }

    func getSecretSharedKey() -> Int? {
        return secretSharedKey
    }
}

enum DHError: Error {
    case notPrime
    case notPrimitiveRoot
}

func isPrimitiveRoot(_ g: Int, modulo n: Int) -> Bool {
    let phi = n - 1
    let primeFactors = distinctPrimeFactors(of: phi)
    for factor in primeFactors {
        if power(g, phi / factor, modulo: n) == 1 {
            return false
        }
    }
    return true
}

func distinctPrimeFactors(of n: Int) -> [Int] {
    var factors: [Int] = []
    var n = n
    for i in 2...Int(sqrt(Double(n))) {
        if n % i == 0 {
            factors.append(i)
            while n % i == 0 {
                n /= i
            }
        }
    }
    if n > 1 {
        factors.append(n)
    }
    return factors
}