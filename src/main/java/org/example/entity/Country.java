package org.example.entity;

public class Country {
    private int id;
    private String name;

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private Country(Country.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder {
        private int id;
        private String name;

        public Country.Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Country.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Country build() {
            return new Country(this);
        }
    }
}
