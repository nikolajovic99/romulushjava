package src;

public class RomulusH {

    private void hirose_128_128_256(int[] h, int[] g, int[] m) {

        int[] key = new int[48];
        int[] hh = new int[16];
        int i;
        Skinny skinny = new Skinny();

        for (i = 0; i < 16; i++) {
            key[i] = g[i];
            g[i] = h[i];
            hh[i] = h[i];
        }

        g[0] ^= 0x01;

        for (i = 0; i < 32; i++) {
            key[i + 16] = m[i];
        }

        skinny.skinnyEncrypt(h, key);
        skinny.skinnyEncrypt(g, key);

        for (i = 0; i < 16; i++) {
            h[i] ^= hh[i];
            g[i] ^= hh[i];
        }

        g[0] ^= 0x01;

    }

    private void initialize(int[] h, int[] g) {

        int i;

        for (i = 0; i < 16; i++) {
            h[i] = 0;
            g[i] = 0;
        }

    }

    private void ipad_256(int[] m, int[] mp, int l, int len8) {

        int i;

        for (i = 0; i < l; i++) {
            if (i < len8) {
                mp[i] = m[i];
            } else if (i == l - 1) {
                mp[i] = (len8 & 0x1f);
            } else {
                mp[i] = 0x00;
            }
        }

    }

    private void ipad_128(int[] m, int[] mp, int l, int len8) {

        int i;

        for (i = 0; i < l; i++) {
            if (i < len8) {
                mp[i] = m[i];
            } else if (i == l - 1) {
                mp[i] = (len8 & 0xf);
            } else {
                mp[i] = 0x00;
            }
        }

    }

    public int cryptoHash(int[] out, int[] in, long inlen) {

        int[] h = new int[16];
        int[] g = new int[16];
        long mlen;
        int[] p = new int[32];
        int i;

        mlen = inlen;

        initialize(h, g);

        while (mlen >= 32) {
            hirose_128_128_256(h, g, in);
            for (int j = 0; j < in.length; j++){
                in[j]+=32;
            }
            mlen -= 32;
        }

        ipad_256(in, p, 32, (int) mlen);
        h[0] ^= 2;
        hirose_128_128_256(h, g, p);

        for (i = 0; i < 16; i++) {
            out[i] = h[i];
            out[i + 16] = g[i];
        }

        return 0;
    }

}
