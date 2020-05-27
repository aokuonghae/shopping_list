package org.myproject.shopping_list.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ItemNotFoundException extends Exception{

        private static final long serialVersionUID = 1L;

        public ItemNotFoundException(String message) {
            super(message);
        }

        public ItemNotFoundException(String message, Throwable t) {
            super(message, t);
        }

}
