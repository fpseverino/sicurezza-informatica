//
//  dh.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 04/05/23.
//

import Foundation

class DH {
    private let q: Int
    private let a: Int
    private let privateKey: Int
    private let _myPublicKey: Int

    private var _otherPublicKey: Int?
    private var _secretSharedKey: Int?

    var myPublicKey: Int {
        return _myPublicKey
    }

    var otherPublicKey: Int? {
        get {
            return _otherPublicKey
        }
        set {
            if let newValue = newValue {
                _otherPublicKey = newValue
                _secretSharedKey = power(newValue, privateKey, modulo: q)
            }
        }
    }

    var secretSharedKey: Int? {
        return _secretSharedKey
    }

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
        _myPublicKey = power(a, privateKey, modulo: q)
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