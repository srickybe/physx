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
class RigidBody {

    private final double mass;
    private final Matrix33 inertia;
    private final Matrix33 inertiaInv;
    private State state;
    private Vector3 linVel;
    private Vector3 angVel;
    private Matrix33 orientM;
    private Matrix33 worldInertia;
    private Vector3 force;
    private Vector3 torque;

    private RigidBody() {
        mass = Double.MAX_VALUE;
        inertia = Matrix33.ident().mult(Double.MAX_VALUE);
        inertiaInv = Matrix33.zero();
        state = new State(
                new Vector3(0.0, 0.0, 0.0),
                new Quaternion(1.0, 0.0, 0.0, 0.0),
                new Vector3(0.0, 0.0, 0.0),
                new Vector3(0.0, 0.0, 0.0)
        );
        force = new Vector3(0.0, 0.0, 0.0);
        torque = new Vector3(0.0, 0.0, 0.0);
        computeAux();
    }

    RigidBody(
            double mass,
            Matrix33 inertia,
            Matrix33 inertiaInv,
            State state
    ) {
        this.mass = mass;
        this.inertia = inertia;
        this.inertiaInv = inertiaInv;
        this.state = state;
        this.force = new Vector3(0.0, 0.0, 0.0);
        this.torque = new Vector3(0.0, 0.0, 0.0);

        computeAux();
    }

    private void computeAux() {
        linVel = Vector3.mult(getLinMom(), 1.0 / mass);
        orientM = getOrientQ().toRotationMatrix();
        worldInertia = orientM.mult(inertia).mult(Matrix33.transp(orientM));
        angVel = Matrix33.invert(worldInertia).mult(getAngMom());
    }

    public void applyForce(Vector3 f, Vector3 x) {
        force.add(f);
        torque.add(Vector3.sub(x, getPos()).cross(f));
    }

    public void applyForceToCenter(Vector3 f) {
        this.force.add(f);
    }

    public void applyTorque(Vector3 t) {
        this.torque.add(t);
    }

    public State computeStateDerivative(State s) {
        Vector3 dxdt = new Vector3(s.getLinMom()).mult(1.0 / mass);
        Matrix33 Rt = s.getOrientQ().toRotationMatrix();
        Matrix33 invIt = Matrix33.transp(Rt).mult(inertiaInv).mult(Rt);
        Vector3 w = invIt.mult(s.getAngMom());
        Quaternion dqdt = new Quaternion(0.0, w.get(0), w.get(1), w.get(2)).mult(s.getOrientQ()).mult(0.5);

        return new State(dxdt, dqdt, force, torque);
    }

    public Vector3 convertToLocal(Vector3 world) {
        return Matrix33.invert(getOrientM()).mult(world);
    }

    public Vector3 convertToWorld(Vector3 local) {
        return getOrientM().mult(local);
    }

    public Vector3 getPos() {
        return state.getPos();
    }

    public Vector3 getAngMom() {
        return state.getAngMom();
    }

    public Matrix33 getInertia() {
        return inertia;
    }

    public Matrix33 getinertiaInv() {
        return inertiaInv;
    }

    public Vector3 getForce() {
        return force;
    }

    public double getMass() {
        return mass;
    }

    public Vector3 getLinMom() {
        return state.getLinMom();
    }

    public Vector3 getLinVel() {
        return linVel;
    }

    public Quaternion getOrientQ() {
        return state.getOrientQ();
    }

    public Matrix33 getOrientM() {
        return orientM;
    }

    public Vector3 getPosition() {
        return state.getPos();
    }

    public Matrix33 getWorldInertia() {
        return worldInertia;
    }

    public State getState() {
        return state;
    }

    public Vector3 getTorque() {
        return torque;
    }

    public void resetForce() {
        force = new Vector3(0.0, 0.0, 0.0);
    }

    public void resetTorque() {
        torque = new Vector3(0.0, 0.0, 0.0);
    }

    @Override
    public String toString() {
        String res = "{"
                + "\nmass: " + mass
                + "\ninertia:\n" + inertia
                + "\ninertiaInv:\n" + inertiaInv
                + "\nstate:\n" + state
                + "\nlinVel = " + linVel
                + "\nangVel = " + angVel
                + "\norientM:\n" + orientM
                + "\nforce:" + force
                + "\ntorque:" + torque
                + "\n";

        return res;
    }

    public void updateRK4(double dt) {
        State[] s = new State[4];
        State[] k = new State[4];
        double[] h = {dt / 2, dt / 2, dt};

        s[0] = state;
        k[0] = this.computeStateDerivative(s[0]);

        for (int i = 1; i < 4; ++i) {
            s[i] = new State(k[i - 1]).mult(h[i - 1]).add(state);
            k[i] = this.computeStateDerivative(s[i]);
        }

        double[] c = {1, 2, 2, 1};
        State res = State.zero();

        for (int i = 0; i < 4; ++i) {
            res.add(State.mult(k[i], c[i]));
        }

        res.mult(dt / 6.0);
        res.add(state);
        res.getOrientQ().norm();
        state = res;
        computeAux();
    }

    public static void main(String args[]) {
        double m = 5.0;
        double r = 0.05;
        double R = 100.0;
        double w = 0.5;
        double f = m * R * w * w;
        double I = 0.4 * m * r * r;
        double ang = Math.PI / 2;;
        double qw = Math.cos(ang);
        Vector3 qv = new Vector3(0.0, 0.0, 1.0).mult(Math.sin(ang));
        Matrix33 Ib = Matrix33.ident().mult(I);
        Matrix33 invIb = Matrix33.invert(Ib);

        Quaternion qt = new Quaternion(qw, qv.get(0), qv.get(1), qv.get(2));
        Matrix33 Rt = qt.toRotationMatrix();

        Vector3 xt = new Vector3(0.0, R, 0.0);
        Vector3 vt = Rt.mult(new Vector3(1.0, 0.0, 0.0).mult(R * w));
        Vector3 Pt = Vector3.mult(vt, m);

        Matrix33 wI = Rt.mult(Ib).mult(Matrix33.transp(Rt));
        Vector3 Lt = wI.mult(new Vector3(0.0, 0.0, w));
        State st = new State(xt, qt, Pt, Lt);
        RigidBody rb = new RigidBody(m, Ib, invIb, st);

        double t = 0.0;
        double dt = 1.0 / 60.0;

        while (t < 10) {
            rb.resetForce();
            Vector3 uy = rb.convertToWorld(new Vector3(0.0, 1.0, 0.0));
            Vector3 cf = Vector3.mult(uy, f);
            rb.applyForceToCenter(cf);
            rb.updateRK4(dt);
            t += dt;
            System.out.println("rb = " + rb);
        }
    }
}
