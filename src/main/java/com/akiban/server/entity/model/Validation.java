/**
 * END USER LICENSE AGREEMENT (“EULA”)
 *
 * READ THIS AGREEMENT CAREFULLY (date: 9/13/2011):
 * http://www.akiban.com/licensing/20110913
 *
 * BY INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE, YOU ARE ACCEPTING
 * ALL OF THE TERMS AND CONDITIONS OF THIS AGREEMENT. YOU AGREE THAT THIS
 * AGREEMENT IS ENFORCEABLE LIKE ANY WRITTEN AGREEMENT SIGNED BY YOU.
 *
 * IF YOU HAVE PAID A LICENSE FEE FOR USE OF THE SOFTWARE AND DO NOT AGREE TO
 * THESE TERMS, YOU MAY RETURN THE SOFTWARE FOR A FULL REFUND PROVIDED YOU (A) DO
 * NOT USE THE SOFTWARE AND (B) RETURN THE SOFTWARE WITHIN THIRTY (30) DAYS OF
 * YOUR INITIAL PURCHASE.
 *
 * IF YOU WISH TO USE THE SOFTWARE AS AN EMPLOYEE, CONTRACTOR, OR AGENT OF A
 * CORPORATION, PARTNERSHIP OR SIMILAR ENTITY, THEN YOU MUST BE AUTHORIZED TO SIGN
 * FOR AND BIND THE ENTITY IN ORDER TO ACCEPT THE TERMS OF THIS AGREEMENT. THE
 * LICENSES GRANTED UNDER THIS AGREEMENT ARE EXPRESSLY CONDITIONED UPON ACCEPTANCE
 * BY SUCH AUTHORIZED PERSONNEL.
 *
 * IF YOU HAVE ENTERED INTO A SEPARATE WRITTEN LICENSE AGREEMENT WITH AKIBAN FOR
 * USE OF THE SOFTWARE, THE TERMS AND CONDITIONS OF SUCH OTHER AGREEMENT SHALL
 * PREVAIL OVER ANY CONFLICTING TERMS OR CONDITIONS IN THIS AGREEMENT.
 */

package com.akiban.server.entity.model;

import com.akiban.util.ArgumentValidation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public final class Validation implements Comparable<Validation> {

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Validation that = (Validation) o;
        return name.equals(that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, value);
    }

    @Override
    public int compareTo(Validation o) {
        int keyCompares = name.compareTo(o.name);
        if (keyCompares != 0)
            return keyCompares;
        if (value == null)
            return o.value == null ? 0 : -1;
        else if (o.value == null)
            return 1;
        String valueString = value.toString();
        String oValueString = o.value.toString();
        return valueString.compareTo(oValueString);
    }

    public static Set<Validation> createValidations(Collection<Map<String, ?>> validations) {
        Set<Validation> result = new TreeSet<>();
        for (Map<String, ?> validation : validations) {
            if (!result.add(new Validation(validation)))
                throw new IllegalEntityDefinition("duplicate validation:" + validation);
        }
        return Collections.unmodifiableSet(result);
    }

    Validation(Map<String, ?> validation) {
        if (validation.size() != 1)
            throw new IllegalEntityDefinition("illegal validation definition (map must have one entry)");
        Map.Entry<String, ?> entry = validation.entrySet().iterator().next();
        this.name = entry.getKey();
        this.value = entry.getValue();
    }

    // for testing
    public Validation(String name, Object value) {
        ArgumentValidation.notNull("validation name", name);
        this.name = name;
        this.value = value;
    }

    private final String name;
    private final Object value;
}