package casebook.util;

import casebook.controllers.BaseController;

import java.lang.reflect.Method;

public class ControllerActionPair {
    private BaseController baseController;
    private Method action;

    public ControllerActionPair(BaseController baseController, Method action) {
        this.setBaseController(baseController);
        this.setMethod(action);
    }

    public BaseController getBaseController() {
        return this.baseController;
    }

    private void setBaseController(BaseController baseController) {
        this.baseController = baseController;
    }

    public Method getAction() {
        return this.action;
    }

    private void setMethod(Method method) {
        this.action = method;
    }
}

