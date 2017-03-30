package com.github.bachelorpraktikum.dbvisualization.database.model;

import java.util.Optional;

enum FixAttributeValues {
    ZUGFOLGE(85, "ActiveZugFolge", "ZugFolgeImpl"),
    HAUPTSIGNAL(1, "HauptSignal", null),
    HAUPTSIGNAL_MIT_SPERRSIGNAL(2, null, null),
    VORSIGNAL(3, "VorSignal", null),
    SPERRSIGNAL(4, "SperrSignal", null),
    HAUPT_UND_VORSIGNAL(5, null, null),
    HAUPT_UND_VORSIGNAL_MIT_SPERRSIGNAL(6, null, null),
    WEICHEN_PUNKT(32, "WeichenPunkt", null);

    private final int id;
    private final String lhs;
    private final String rhs;

    FixAttributeValues(int id, String lhs, String rhs) {
        this.id = id;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    int getId() {
        return id;
    }

    /**
     * Returns the left hand side class name for the attribute. If this is null, the attribute can't
     * be instantiated as one abs element only, it's a composition of different elements. This has
     * to be handled elsewhere.
     *
     * @return Lefthand side class name if is single abs element, null otherwise
     */
    String classNameLhs() {
        return lhs;
    }

    String classNameRhs() {
        if (rhs == null) {
            return String.format("%sImpl", lhs);
        }

        return rhs;
    }

    static Optional<FixAttributeValues> get(int id) {
        for (FixAttributeValues attributeValue : FixAttributeValues.values()) {
            if (id == attributeValue.getId()) {
                return Optional.of(attributeValue);
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s", getId(), classNameLhs(), classNameRhs());
    }
}
