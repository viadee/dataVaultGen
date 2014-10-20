package de.viadee.dv.service.supplement;

import de.viadee.dv.model.Schema;

/**
 * Interface that provides functionality to validate a given {@link Schema}. A set of rules will be defined in the
 * implementation and can be expanded with new features added to the generator.
 * 
 * @author Lena
 *
 */
public interface SchemaValidator {

    /**
     * 
     * @param schema
     * @return true if given schema is valid.
     */
    public boolean validateSchema(Schema schema);

}
