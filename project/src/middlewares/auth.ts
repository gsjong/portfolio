import { Request, Response } from 'express';
import jwt from 'jsonwebtoken';
import { User } from '../models/User';

export const auth = (req: Request, res: Response, next: () => void) => {
    const token = req.cookies.token;
    if (!token) {
        req.body.authType = "guest";
        next();
    }
    else {
        try {
            jwt.verify(String(token), String(process.env.SECRET_TOKEN), (err: any, decoded: any) => {
                if (err) throw err;
                User.findById(decoded.data)
                    .then((user) => {
                        if (!user) {
                            return res.status(400).json({
                                status: 'fail',
                                error: 'who are you?',
                            })
                        }
                        else {
                            req.body.authUser = String(user._id);
                            req.body.authType = user.type;
                            next();
                        }
                    })
            })
        }
        catch (error) {
            res.cookie('token', null, {
                maxAge: 0,
            })
            res.redirect("/");
        }
    }
}