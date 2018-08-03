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
class Vector3 {

    public static Vector3 zero() {
        return new Vector3(0.0, 0.0, 0.0);
    }

    private double cmp[];

    private Vector3() {
    }

    public Vector3(double x, double y, double z) {
        cmp = new double[]{x, y, z};
    }

    public Vector3(Vector3 vec) {
        this(vec.cmp[0], vec.cmp[1], vec.cmp[2]);
    }

    public Vector3 add(Vector3 rhs) {
        cmp[0] += rhs.cmp[0];
        cmp[1] += rhs.cmp[1];
        cmp[2] += rhs.cmp[2];

        return this;
    }

    public static Vector3 add(Vector3 lhs, Vector3 rhs) {
        return new Vector3(
                lhs.cmp[0] + rhs.cmp[0],
                lhs.cmp[1] + rhs.cmp[1],
                lhs.cmp[2] + rhs.cmp[2]);
    }

    public Vector3 cross(Vector3 rhs) {
        return new Vector3(
                cmp[1] * rhs.cmp[2] - cmp[2] * rhs.cmp[1],
                cmp[2] * rhs.cmp[0] - cmp[0] * rhs.cmp[2],
                cmp[0] * rhs.cmp[1] - cmp[1] * rhs.cmp[0]
        );
    }

    public double dot(Vector3 rhs) {
        return cmp[0] * rhs.cmp[0] + cmp[1] * rhs.cmp[1]
                + cmp[2] * rhs.cmp[2];
    }

    public double get(int i) {
        return cmp[i];
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    private double lengthSquared() {
        return dot(this);
    }

    public Vector3 negate() {
        cmp[0] = -cmp[0];
        cmp[1] = -cmp[1];
        cmp[2] = -cmp[2];

        return this;
    }

    public static Vector3 negate(Vector3 rhs) {
        return new Vector3(
                -rhs.cmp[0],
                -rhs.cmp[1],
                -rhs.cmp[2]
        );
    }

    public Vector3 norm() {
        mult(1.0 / length());

        return this;
    }

    public Vector3 norm(Vector3 rhs) {
        Vector3 res = new Vector3(rhs);
        res.norm();

        return res;
    }

    public Vector3 mult(double rhs) {
        cmp[0] *= rhs;
        cmp[1] *= rhs;
        cmp[2] *= rhs;

        return this;
    }

    public static Vector3 mult(Vector3 lhs, double rhs) {
        return new Vector3(lhs.cmp[0] * rhs, lhs.cmp[1] * rhs, lhs.cmp[2] * rhs);
    }

    public Vector3 sub(Vector3 rhs) {
        cmp[0] -= rhs.cmp[0];
        cmp[1] -= rhs.cmp[1];
        cmp[2] -= rhs.cmp[2];

        return this;
    }

    public static Vector3 sub(Vector3 lhs, Vector3 rhs) {
        return new Vector3(
                lhs.cmp[0] - rhs.cmp[0],
                lhs.cmp[1] - rhs.cmp[1],
                lhs.cmp[2] - rhs.cmp[2]
        );
    }

    @Override
    public String toString() {
        String res = "{";

        for (int i = 0; i < 3; ++i) {
            res += String.format("%10.3f", cmp[i]);
        }

        res += "}";

        return res;
    }
}