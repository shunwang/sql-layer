/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.qp.physicaloperator;

import com.akiban.qp.expression.Expression;
import com.akiban.qp.row.ProjectedRow;
import com.akiban.qp.row.Row;
import com.akiban.qp.rowtype.ProjectedRowType;
import com.akiban.qp.rowtype.RowType;
import com.akiban.util.ArgumentValidation;

import java.util.ArrayList;
import java.util.List;

class Project_Default extends PhysicalOperator
{
    // Object interface

    @Override
    public String toString()
    {
        return String.format("project(%s)", projections);
    }

    // PhysicalOperator interface

    @Override
    protected Cursor cursor(StoreAdapter adapter)
    {
        return new Execution(adapter, inputOperator.cursor(adapter));
    }

    public ProjectedRowType rowType()
    {
        return projectType;
    }

    @Override
    public List<PhysicalOperator> getInputOperators()
    {
        List<PhysicalOperator> result = new ArrayList<PhysicalOperator>(1);
        result.add(inputOperator);
        return result;
    }

    @Override
    public String describePlan()
    {
        return describePlan(inputOperator);
    }

    // Project_Default interface

    public Project_Default(PhysicalOperator inputOperator, RowType rowType, List<Expression> projections)
    {
        ArgumentValidation.notNull("rowType", rowType);
        ArgumentValidation.notEmpty("projections", projections);
        this.inputOperator = inputOperator;
        this.rowType = rowType;
        this.projections = new ArrayList<Expression>(projections);
        projectType = rowType.schema().newProjectType(this.projections);
    }

    // Object state

    private final PhysicalOperator inputOperator;
    private final RowType rowType;
    private final List<Expression> projections;
    private final ProjectedRowType projectType;

    // Inner classes

    private class Execution implements Cursor
    {
        // Cursor interface

        @Override
        public void open(Bindings bindings)
        {
            input.open(bindings);
        }

        @Override
        public Row next()
        {
            Row projectedRow = null;
            Row inputRow;
            if ((inputRow = input.next()) != null) {
                projectedRow =
                    inputRow.rowType() == rowType
                    ? new ProjectedRow(projectType, inputRow, projections)
                    : inputRow;
            }
            return projectedRow;
        }

        @Override
        public void close()
        {
            input.close();
        }

        // Execution interface

        Execution(StoreAdapter adapter, Cursor input)
        {
            this.input = input;
        }

        // Object state

        private final Cursor input;
    }
}