/*
 * Copyright (c) 2011 Derek Murray <Derek.Murray@cl.cam.ac.uk>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.asgow.ciel.references;


public abstract class Reference {

	private String id;
	
	protected Reference (String id) {
		this.id = id;
	}
	
	protected Reference (com.asgow.ciel.protocol.CielProtos.Reference ref) {
		this.id = ref.getId();
	}
	
	public String getId() {
		return this.id;
	}
	
	abstract public boolean isConsumable();
	
	public abstract com.asgow.ciel.protocol.CielProtos.Reference.Builder buildProtoBuf(com.asgow.ciel.protocol.CielProtos.Reference.Builder builder);
	
	public com.asgow.ciel.protocol.CielProtos.Reference asProtoBuf() {
		com.asgow.ciel.protocol.CielProtos.Reference.Builder builder = com.asgow.ciel.protocol.CielProtos.Reference.newBuilder();
		builder.setId(this.id);
		this.buildProtoBuf(builder);
		return builder.build();
	}
	
	public static Reference fromProtoBuf(com.asgow.ciel.protocol.CielProtos.Reference ref) {
		switch (ref.getType()) {
		case CONCRETE:
			return new ConcreteReference(ref);
		case FUTURE:
			return new FutureReference(ref);
		case STREAM:
			return new StreamReference(ref);
		case SWEETHEART:
			return new SweetheartReference(ref);
		case VALUE:
			return new ValueReference(ref);
		default:
			throw new UnsupportedOperationException("Cannot handle references of type: " + ref.getType());
		}
	}
	
}
