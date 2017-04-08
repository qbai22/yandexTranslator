package qbai22.com.yandextranslator.network.loader;

class Response<D> {

    private Exception mException;

    private D mResult;

    public static <D> Response<D> ok(D data) {

        Response<D> response = new Response<D>();
        response.mResult = data;

        return response;
    }

    public static <D> Response<D> error(Exception ex) {

        Response<D> response = new Response<D>();
        response.mException = ex;

        return response;
    }

    public boolean hasError() {

        return mException != null;
    }

    public Exception getException() {

        return mException;
    }

    public D getResult() {

        return mResult;
    }
}
