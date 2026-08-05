
class Point{
    private int x, y;

    
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }class Driver {
    public static void main(String[] args) {

        Point[] p = {
            new Point(1, 2),
            new Point(3, 4),
            new Point(1, 2),
            new Point(5, 6),
            new Point(3, 4)
        };

        int count = 0;

        for (int i = 0; i < p.length; i++) {
            boolean found = false;

            for (int j = 0; j < i; j++) {
                if (p[i].equals(p[j])) {
                    found = true;
                    break;
                }
            }

            if (!found)
                count++;
        }

        System.out.println("Distinct: " + count);
    }
}

    
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    
    public boolean equals(Object obj) {
        Point p = (Point) obj;
        return x == p.x && y == p.y;
    }
}
