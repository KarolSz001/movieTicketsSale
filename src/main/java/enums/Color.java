package enums;

public enum Color {

    RED("czerwony") {
        @Override
        public void m() {
            super.m();
        }

        @Override
        public void am() {

        }
    },
    GREEN("zielony") {
        @Override
        public void am() {

        }
    },
    BLUE("niebieski") {
        @Override
        public void am() {

        }
    };

    Color(String name) {
        this.name = name;
    }

    private String name;

    public void m() {

    }

    public static void sm() {

    }

    public abstract void am();
}
