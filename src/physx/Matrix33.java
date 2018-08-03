/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author john
 */
package physx;

class Matrix33 {

    private double[][] cmp;

    public static Matrix33 ident() {
        return new Matrix33(
                1.0, 0.0, 0.0,
                0.0, 1.0, 0.0,
                0.0, 0.0, 1.0);
    }

    public static Matrix33 zero() {
        return new Matrix33(
                0.0, 0.0, 0.0,
                0.0, 0.0, 0.0,
                0.0, 0.0, 0.0);
    }

    private Matrix33() {
    }

    public Matrix33(Matrix33 m) {
        this(
                m.cmp[0][0], m.cmp[0][1], m.cmp[0][2],
                m.cmp[1][0], m.cmp[1][1], m.cmp[1][2],
                m.cmp[2][0], m.cmp[2][1], m.cmp[2][2]
        );
    }

    public Matrix33(
            double m00, double m01, double m02,
            double m10, double m11, double m12,
            double m20, double m21, double m22) {
        cmp = new double[][]{
            {m00, m01, m02},
            {m10, m11, m12},
            {m20, m21, m22}
        };
    }

    public Matrix33 add(Matrix33 rhs) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                cmp[i][j] += rhs.cmp[i][j];
            }
        }

        return this;
    }

    public static Matrix33 add(Matrix33 lhs, Matrix33 rhs) {
        Matrix33 res = new Matrix33();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                res.cmp[i][j] = lhs.cmp[i][j] + rhs.cmp[i][j];
            }
        }

        return res;
    }

    public double det() {
        return (cmp[1][1] * cmp[2][2] - cmp[1][2] * cmp[2][1]) * cmp[0][0]
                + (cmp[1][2] * cmp[2][0] - cmp[1][0] * cmp[2][2]) * cmp[0][1]
                + (cmp[1][0] * cmp[2][1] - cmp[1][1] * cmp[2][0]) * cmp[0][2];
    }

    public double get(int i, int j) {
        return cmp[i][j];
    }

    public Matrix33 invert() {
        double[][] res = new double[3][3];

        res[0][0] = (cmp[1][1] * cmp[2][2] - cmp[1][2] * cmp[2][1]);
        res[1][0] = (cmp[1][2] * cmp[2][0] - cmp[1][0] * cmp[2][2]);
        res[2][0] = (cmp[1][0] * cmp[2][1] - cmp[1][1] * cmp[2][0]);

        double invDet = 1.0 / det();
        res[0][0] *= invDet;
        res[0][1] = (cmp[0][2] * cmp[2][1] - cmp[0][1] * cmp[2][2]) * invDet;
        res[0][2] = (cmp[0][1] * cmp[1][2] - cmp[0][2] * cmp[1][1]) * invDet;
        res[1][0] *= invDet;
        res[1][1] = (cmp[0][0] * cmp[2][2] - cmp[0][2] * cmp[2][0]) * invDet;
        res[1][2] = (cmp[0][2] * cmp[1][0] - cmp[0][0] * cmp[1][2]) * invDet;
        res[2][0] *= invDet;
        res[2][1] = (cmp[0][1] * cmp[2][0] - cmp[0][0] * cmp[2][1]) * invDet;
        res[2][2] = (cmp[0][0] * cmp[1][1] - cmp[0][1] * cmp[1][0]) * invDet;

        cmp = res;

        return this;
    }

    public static Matrix33 invert(Matrix33 rhs) {
        Matrix33 res = new Matrix33(rhs);
        res.invert();

        return res;
    }

    public Matrix33 mult(double rhs) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                cmp[i][j] *= rhs;
            }
        }

        return this;
    }

    public static Matrix33 mult(Matrix33 lhs, double rhs) {
        Matrix33 res = new Matrix33(lhs);
        res.mult(rhs);

        return res;
    }

    public Matrix33 mult(Matrix33 rhs) {
        Matrix33 res = Matrix33.zero();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                res.cmp[i][j] = 0;

                for (int k = 0; k < 3; ++k) {
                    res.cmp[i][j] += cmp[i][k] * rhs.cmp[k][j];
                }
            }
        }

        return res;
    }

    public Vector3 mult(Vector3 rhs) {
        return new Vector3(
                cmp[0][0] * rhs.get(0) + cmp[0][1] * rhs.get(1) + cmp[0][2] * rhs.get(2),
                cmp[1][0] * rhs.get(0) + cmp[1][1] * rhs.get(1) + cmp[1][2] * rhs.get(2),
                cmp[2][0] * rhs.get(0) + cmp[2][1] * rhs.get(1) + cmp[2][2] * rhs.get(2)
        );
    }

    public Matrix33 orthonormalize() {
        double inv_mag = 1.0 / Math.sqrt(
                cmp[0][0] * cmp[0][0]
                + cmp[1][0] * cmp[1][0]
                + cmp[2][0] * cmp[2][0]
        );

        cmp[0][0] *= inv_mag;
        cmp[1][0] *= inv_mag;
        cmp[2][0] *= inv_mag;

        double proj
                = cmp[0][0] * cmp[0][1]
                + cmp[1][0] * cmp[1][1]
                + cmp[2][0] * cmp[2][1];

        cmp[0][1] -= cmp[0][0] * proj;
        cmp[1][1] -= cmp[1][0] * proj;
        cmp[2][1] -= cmp[2][0] * proj;

        inv_mag = (double) 1.0 / Math.sqrt(
                cmp[0][1] * cmp[0][1]
                + cmp[1][1] * cmp[1][1]
                + cmp[2][1] * cmp[2][1]
        );

        cmp[0][1] *= inv_mag;
        cmp[1][1] *= inv_mag;
        cmp[2][1] *= inv_mag;

        cmp[0][2] = cmp[1][0] * cmp[2][1] - cmp[2][0] * cmp[1][1];
        cmp[1][2] = cmp[2][0] * cmp[0][1] - cmp[0][0] * cmp[2][1];
        cmp[2][2] = cmp[0][0] * cmp[1][1] - cmp[1][0] * cmp[0][1];

        return this;
    }

    public static Matrix33 orthonormalize(Matrix33 rhs) {
        Matrix33 res = new Matrix33(rhs);
        res.orthonormalize();

        return res;
    }

    public void set(double val, int i, int j) {
        cmp[i][j] = val;
    }

    @Override
    public String toString() {
        String res = "{\n";

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                res += String.format("%10.3f", cmp[i][j]);
            }

            res += "\n";
        }

        res += '}';

        return res;
    }

    public Matrix33 transp() {
        double tmp;

        for (int i = 0; i < 3; ++i) {
            for (int j = i + 1; j < 3; ++j) {
                tmp = cmp[i][j];
                cmp[i][j] = cmp[j][i];
                cmp[j][i] = tmp;
            }
        }

        return this;
    }

    public static Matrix33 transp(Matrix33 rhs) {
        Matrix33 res = new Matrix33(rhs);
        res.transp();

        return res;
    }
}