//
//  crt.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 03/05/23.
//

import Foundation

func crt(m: [Int], a: [Int]) throws -> Int {
    if m.count != a.count {
        throw CRTError.notSameLength
    }
    for i in 0..<m.count {
        for j in i+1..<m.count {
            if gcd(m[i], m[j]) != 1 {
                throw CRTError.notCoprime
            }
        }
    }
    var prod = 1
    for n in m {
        prod *= n
    }
    var result = 0
    for i in 0..<m.count {
        let pp = prod / m[i]
        // TODO: check if inverse can be safely forced unwrapped (AKA if the inverse always exists)
        result += a[i] * inverse(pp, modulo: m[i])! * pp
    }
    return result % prod
}

enum CRTError: Error {
    case notCoprime
    case notSameLength
}