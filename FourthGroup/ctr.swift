//
//  ctr.swift
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 03/05/23.
//

import Foundation

func ctr(num: [Int], rem: [Int]) -> Int? {
    for i in 0..<num.count {
        for j in i+1..<num.count {
            if gcd(num[i], num[j]) != 1 {
                return nil
            }
        }
    }
    var prod = 1
    for n in num {
        prod *= n
    }
    var result = 0
    for i in 0..<num.count {
        let pp = prod / num[i]
        // TODO: check if inverse can be safely forced unwrapped (AKA if the inverse always exists)
        result += rem[i] * inverse(pp, num[i])! * pp
    }
    return result % prod
}