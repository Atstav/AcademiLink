// Sends consistent JSON responses for application errors
const errorHandler = (err, req, res, next) => {
    let statusCode = res.statusCode === 200 ? 500 : res.statusCode;

    if (err.name === "ValidationError" || err.name === "CastError") {
        statusCode = 400;
    }

    if (err.code === 11000) {
        statusCode = 409;
    }

    if (err.name === "JsonWebTokenError" || err.name === "TokenExpiredError") {
        statusCode = 401;
    }

    res.status(statusCode).json({
        success: false,
        message: err.message
    });
};

module.exports = errorHandler;