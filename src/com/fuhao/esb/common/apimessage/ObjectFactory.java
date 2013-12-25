package com.fuhao.esb.common.apimessage;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.css.sword.aip.Packet package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
//@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.css.sword.aip.Packet
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of RtnMsg
	 * 
	 */
	public RtnMeg createRtnMsg() {
		return new RtnMeg();
	}

	/**
	 * Create an instance of {@link HeadType }
	 * 
	 */
	public HeadType createHeadType() {
		return new HeadType();
	}

	/**
	 * Create an instance of ParamListType
	 * 
	 */
	public Expand createParamListType() {
		return new Expand();
	}

	/**
	 * Create an instance of {@link Service }
	 * 
	 */
	public Service createService() {
		return new Service();
	}

}
