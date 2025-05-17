package org.fcup.model.auction;

import org.fcup.model.user.User;

import java.util.Objects;

public class Item {
    private Long minimumValue;
    private String name;
    private User owner;

    public Item(final Long minimumValue, final String name, final User owner) {
        validateMinimumValue(minimumValue);
        validateName(name);
        //validateOwner(owner);

        this.name = name;
        this.minimumValue = minimumValue;
        this.owner = owner;
    }

    public long getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(final Long minimumValue) {
        validateMinimumValue(minimumValue);

        this.minimumValue = minimumValue;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        validateName(name);

        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void changeOwner(final User owner) {
        validateOwner(owner);

        this.owner = owner;
    }

    private void validateMinimumValue(final Long minimumValue) {
        if (minimumValue == null || minimumValue < 0)
            throw new IllegalArgumentException("Minimum value invalid");
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid Item name");
    }

    private void validateOwner(final User owner) {
        if (owner == null)
            throw new NullPointerException("No item owner provided;\nPlease provide a valid item owner;");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;

        Item item = (Item) o;
        return Objects.equals(minimumValue, item.minimumValue) && Objects.equals(name, item.name) && Objects.equals(owner, item.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimumValue, name, owner);
    }
}