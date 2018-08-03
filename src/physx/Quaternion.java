/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physx;

/**
 *
 * @author srick
 */
class Quaternion {

    private double[] cmp;

    public static Quaternion zero() {
        return new Quaternion(0.0, 0.0, 0.0, 0.0);
    }

    private Quaternion() {
    }

    public Quaternion(Quaternion q) {
        this(q.cmp[0], q.cmp[1], q.cmp[2], q.cmp[3]);
    }

    public Quaternion(double fW, double fX, double fY, double fZ) {
        cmp = new double[]{fW, fX, fY, fZ};
    }

    public static Quaternion add(Quaternion lhs, Quaternion rhs) {
        return new Quaternion(
                lhs.cmp[0] + rhs.cmp[0],
                lhs.cmp[1] + rhs.cmp[1],
                lhs.cmp[2] + rhs.cmp[2],
                lhs.cmp[3] + rhs.cmp[3]
        );
    }

    public Quaternion add(Quaternion rhs) {
        cmp[0] += rhs.cmp[0];
        cmp[1] += rhs.cmp[1];
        cmp[2] += rhs.cmp[2];
        cmp[3] += rhs.cmp[3];

        return this;
    }

    public Matrix33 toRotationMatrix() {
        double xx = cmp[1] * cmp[1];
        double xy = cmp[1] * cmp[2];
        double xz = cmp[1] * cmp[3];
        double xw = cmp[1] * cmp[0];

        double yy = cmp[2] * cmp[2];
        double yz = cmp[2] * cmp[3];
        double yw = cmp[2] * cmp[0];

        double zz = cmp[3] * cmp[3];
        double zw = cmp[3] * cmp[0];

        return new Matrix33(
                1 - 2 * (yy + zz),
                2 * (xy - zw),
                2 * (xz + yw),
                2 * (xy + zw),
                1 - 2 * (xx + zz),
                2 * (yz - xw),
                2 * (xz - yw),
                2 * (yz + xw),
                1 - 2 * (xx + yy));
    }
    
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return cmp[1] * cmp[1]
                + cmp[2] * cmp[2]
                + cmp[3] * cmp[3]
                + cmp[0] * cmp[0];
    }

    public Quaternion mult(double rhs) {
        cmp[0] *= rhs;
        cmp[1] *= rhs;
        cmp[2] *= rhs;
        cmp[3] *= rhs;

        return this;
    }

    public static Quaternion mult(Quaternion lhs, double rhs) {
        return new Quaternion(
                lhs.cmp[0] * rhs,
                lhs.cmp[1] * rhs,
                lhs.cmp[2] * rhs,
                lhs.cmp[3] * rhs
        );
    }

    public Quaternion mult(Quaternion rhs) {
        return new Quaternion(
                cmp[0] * rhs.cmp[0] - cmp[1] * rhs.cmp[1]
                - cmp[2] * rhs.cmp[2] - cmp[3] * rhs.cmp[3],
                cmp[0] * rhs.cmp[1] + cmp[1] * rhs.cmp[0]
                + cmp[2] * rhs.cmp[3] - cmp[3] * rhs.cmp[2],
                cmp[0] * rhs.cmp[2] - cmp[1] * rhs.cmp[3]
                + cmp[2] * rhs.cmp[0] + cmp[3] * rhs.cmp[1],
                cmp[0] * rhs.cmp[3] + cmp[1] * rhs.cmp[2]
                - cmp[2] * rhs.cmp[1] + cmp[3] * rhs.cmp[0]
        );
    }

    public Quaternion norm() {
        mult(1.0 / length());

        return this;
    }

    public static Quaternion norm(Quaternion q) {
        Quaternion res = new Quaternion(q);
        res.norm();

        return res;
    }

    @Override
    public String toString() {
        String res = "{";

        for (int i = 0; i < 4; ++i) {
            res += String.format("%10.3f", cmp[i]);
        }

        res += "}";

        return res;
    }
}
