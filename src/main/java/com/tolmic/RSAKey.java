package com.tolmic;

import lombok.Data;

@Data
public class RSAKey {
    private long n;
    private long e;

    public RSAKey(long n, long e) {
        this.n = n;
        this.e = e;
    }
}
