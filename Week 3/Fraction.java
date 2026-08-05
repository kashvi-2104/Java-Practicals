class Fraction {
    private int num, den;

    
    Fraction(int num, int den) {
        int g = gcd(num, den);
        this.num = num / g;
        this.den = den / g;
    }

    
    int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    
    public String toString() {
        return num + "/" + den;
    }

    
    public boolean equals(Object obj) {
        Fraction f = (Fraction) obj;
        return num == f.num && den == f.den;
    }

    
    public int hashCode() {
        return num + den;
    }
}
