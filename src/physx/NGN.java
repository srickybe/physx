/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physx;

public class NGN {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Matrix33 m = new Matrix33(1.0, 0.0, 3.0, 0.0, 2.0, 0.0, 1.0, 1.0, 1.0);
        System.out.println("m = " + m);
        m.orthonormalize();
        System.out.println("m = " + m);
        double ang = Math.PI / 3;
        Vector3 axis = new Vector3(1.0, 5.6, -2.0).norm();
        axis.mult(Math.sin(ang));
        Quaternion q = new Quaternion(
                Math.cos(ang),
                axis.get(0),
                axis.get(1),
                axis.get(2));
        System.out.println("q = " + q);
        Matrix33 orient = q.toRotationMatrix();
        System.out.println("orient = " + orient);*/

        RigidBody.main(args);
    }
}
