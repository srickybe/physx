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
class State {

    private Vector3 pos;
    private Quaternion orientQ;
    private Vector3 linMom;
    private Vector3 angMom;

    public static State zero() {
        return new State(
                Vector3.zero(),
                Quaternion.zero(),
                Vector3.zero(),
                Vector3.zero());
    }

    private State() {
    }

    public State(
            Vector3 pos,
            Quaternion orientQ,
            Vector3 linMom,
            Vector3 angMom
    ) {
        this.pos = pos;
        this.linMom = linMom;
        this.angMom = angMom;
        this.orientQ = orientQ;
    }

    public State(State rhs) {
        pos = new Vector3(rhs.pos);
        orientQ = new Quaternion(rhs.orientQ);
        linMom = new Vector3(rhs.linMom);
        angMom = new Vector3(rhs.angMom);
    }

    public Vector3 getPos() {
        return pos;
    }

    public Vector3 getLinMom() {
        return linMom;
    }

    public Vector3 getAngMom() {
        return angMom;
    }

    public Quaternion getOrientQ() {
        return orientQ;
    }

    public State add(State rhs) {
        pos.add(rhs.pos);
        linMom.add(rhs.linMom);
        angMom.add(rhs.angMom);
        orientQ.add(rhs.orientQ);

        return this;
    }

    public static State add(State lhs, State rhs) {
        State res = new State(lhs);
        res.add(rhs);

        return res;
    }

    public State mult(double rhs) {
        pos.mult(rhs);
        linMom.mult(rhs);
        angMom.mult(rhs);
        orientQ.mult(rhs);

        return this;
    }

    public static State mult(State lhs, double rhs) {
        State res = new State(lhs);
        res.mult(rhs);

        return res;
    }

    @Override
    public String toString() {
        String res = "{\n"
                + "pos = " + pos
                + "\norientQ = " + orientQ
                + "\nlinMom = " + linMom
                + "\nangMom = " + angMom
                + "\n}";

        return res;
    }

    public static void main(String[] args) {
        State s1 = new State();
        System.out.println("s1 :\n" + s1);
        State s2 = new State(s1);
        System.out.println("s2 :\n" + s2);
        State s3 = new State(
                new Vector3(1.0, 2.0, 3.0),
                new Quaternion(4.0, 5.0, 6.0, 7.0),
                new Vector3(8.0, 9.0, 10.0),
                new Vector3(11.0, 12.0, 13.0)
        );

        s2.add(s3);
        System.out.println("s1 :\n" + s1);
        System.out.println("s2 :\n" + s2);
    }
}