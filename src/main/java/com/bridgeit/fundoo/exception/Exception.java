package com.bridgeit.fundoo.exception;

public class Exception extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public Exception(String message,int errorcode) {
			super(message);
			this.errorcode = errorcode;
		}

			public int getErrorcode() {
				return errorcode;
			}

			public void setErrorcode(int errorcode) {
				this.errorcode = errorcode;
			}

			private int errorcode;
		}


