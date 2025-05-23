/*
 * Copyright 2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.webapp.wacar.resource;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Represents a resource able to serialize itself to JSON.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.0
 * @since 1.0
 */
public interface Resource {

	/**
	 * Returns a JSON representation of the {@code Resource} into the given {@code OutputStream}.
	 *
	 * @param out the stream to which the JSON representation of the {@code Resource} has to be written.
	 *
	 * @throws IOException if something goes wrong while serializing the {@code Resource}.
	 */
	void toJSON(OutputStream out) throws IOException;

}
