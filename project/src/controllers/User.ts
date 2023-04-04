import { User } from '../models/User';
import { User as UserInterface } from '../types/user';
import { Request, Response } from 'express';
import { Wishlist } from '../models/Wishlist';
import jwt from 'jsonwebtoken';
import { Product } from '../models/Product';
import fs from 'fs';

export const getAllUsers = (req: Request, res: Response) => {
    if (req.body.authType === 'admin') {
        User.find()
            .then((users) =>
                res.status(200).json({
                    status: 'success',
                    data: users,
                })
            )
            .catch((error) =>
                res.status(400).json({
                    status: 'fail',
                    error: error.message,
                })
            );
    }
    else {
        res.status(200).json({
            status: 'fail',
            error: '관리자만 열람할 수 있습니다.',
        })
    }
};

export const getOneUser = (req: Request, res: Response) => {
    User.findById(req.body.authUser)
        .then((user) => {
            if (!user) {
                res.status(400).json({
                    status: 'fail',
                    error: 'user not found',
                })
            }
            else {
                res.status(200).json({
                    status: 'success',
                    data: user,
                })
            }
        })
        .catch((error) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const createUser = (req: Request, res: Response) => {
    if (req.body.type === "admin") {
        return res.status(200).json({
            status: 'fail',
            error: 'admin type으로 회원가입할 수 없습니다.',
        })
    }
    try {
        const user = new User(req.body);
        user
            .save()
            .then((newUser: UserInterface) =>
                res.status(200).json({
                    status: 'success',
                    data: newUser,
                })
            )
            .catch((error: any) =>
                res.status(400).json({
                    status: 'fail',
                    error: error.message,
                })
            );
        if (user.type === "buyer") {
            const wishlist = new Wishlist({ "userId": user._id });
            wishlist.save()
        }
    } catch (error: any) {
        res.status(400).json({
            status: 'fail',
            error: error.message,
        });
    }
};

export const updateUser = (req: Request, res: Response) => {
    if (req.body.authType === "guest") {
        return res.redirect("/login");
    }
    const id = req.body.authUser;
    User.findById(id)
        .then((user) => {
            if (!user) {
                res.status(200).json({
                    status: 'fail',
                    error: 'user not found',
                });
            }
            else if (user.password !== req.body.password) {
                res.status(200).json({
                    status: 'fail',
                    error: '현재 비밀번호가 일치하지 않습니다.',
                });
            }
            else {
                user.password = req.body.newPassword;
                user.save()
                    .then(() => {
                        res.status(200).json({
                            status: 'success',
                            data: user,
                        });
                    })
            }
        })
        .catch((error) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const deleteUser = (req: Request, res: Response) => {
    if (req.body.authType === "guest") {
        return res.redirect("/login");
    }
    var id = req.body.authUser;
    if (req.body.authType === "admin" && req.body._id) {
        id = req.body._id;
    }
    var imp = 0;
    Product.findOne({ "status": "In progress", "userId": id })
        .then((product) => {
            if (product) {
                imp = 1;
            }
        })
    Product.findOne({ "status": "In progress", "buyerId": id })
        .then((product) => {
            if (product) {
                imp = 1;
            }
        })
    setTimeout(() => {
        User.findById(id)
            .then((user) => {
                if (!user) {
                    return res.status(200).json({
                        status: 'fail',
                        error: 'user not found',
                    });
                }
                else if (user.password !== req.body.password && (req.body.authType !== 'admin' || !req.body._id)) {
                    return res.status(200).json({
                        status: 'fail',
                        error: '현재 비밀번호가 일치하지 않습니다.',
                    });
                }
                else if (imp === 1 && req.body.authType !== 'admin') {
                    return res.status(200).json({
                        status: 'fail',
                        error: '경매 중인 상품이 있습니다.',
                    });
                }
                if (user.type === 'buyer') {
                    if (imp === 1) {
                        Product.find({ "status": "In progress", "buyerId": id })
                            .then((product) => {
                                product.forEach((item) => {
                                    item.buyerId = "";
                                    item.buyername = "없음";
                                    item.buyerPrice = item.price;
                                    item.save();
                                })
                            })
                    }
                    Wishlist.findOne({ "userId": id })
                        .then((wishlist) => {
                            if (!wishlist) {
                                return res.status(200).json({
                                    status: 'fail',
                                    error: 'wishlist not found',
                                })
                            }
                            else {
                                wishlist.productId.forEach(item => {
                                    Product.findById(item)
                                        .then((product) => {
                                            if (!product) {
                                                return res.status(200).json({
                                                    status: 'fail',
                                                    error: 'product not found',
                                                })
                                            }
                                            else {
                                                product.wish -= 1;
                                                product.save();
                                            }
                                        })
                                });
                                wishlist.remove();
                            }
                        })
                        .catch(() => { })
                }
                else if (user.type === 'seller') {
                    Product.find({ "userId": id })
                        .then((product) => {
                            product.forEach((item) => {
                                if (item.image) {
                                    try {
                                        fs.unlinkSync(item.image);
                                    } catch (error) { }
                                }
                                item.remove();
                            })
                        })
                        .catch(() => { })
                }
                user.remove()
                    .then(() => {
                        if (req.body.authType === "admin" && req.body._id) {
                            if (req.body._id === req.body.authUser) {
                                res.cookie('token', null, {
                                    maxAge: 0,
                                })
                            }
                        }
                        else {
                            res.cookie('token', null, {
                                maxAge: 0,
                            })
                        }
                        res.status(200).json({
                            status: 'success',
                            message: 'user deleted successfully',
                        })
                    })
            })
            .catch((error: any) =>
                res.status(400).json({
                    status: 'fail',
                    error: error.message,
                })
            );
    }, 200);
};

export const login = (req: Request, res: Response) => {
    User.findOne({ "username": req.body.username, "password": req.body.password })
        .then((user) => {
            if (!user) {
                res.status(200).json({
                    status: 'fail',
                    error: '아이디 또는 비밀번호가 일치하지 않습니다.',
                })
            }
            else {
                const token = jwt.sign(
                    {
                        data: user._id.toHexString(),
                        exp: Math.floor(Date.now() / 1000) + 60 * 60 * 24,
                    },
                    process.env.SECRET_TOKEN as string
                );
                res.cookie('token', token);
                res.status(200).json({
                    status: 'success',
                    'token': token,
                })
            }
        })
        .catch((error: any) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const logout = (req: Request, res: Response) => {
    User.findById(req.body.authUser)
        .then((user) => {
            if (!user) {
                res.redirect("/");
            }
            else {
                res.cookie('token', null, {
                    maxAge: 0,
                })
                res.redirect("/");
            }
        })
        .catch((error: any) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const checkUser = (req: Request, res: Response) => {
    User.findOne({ "username": req.body.username })
        .then((user) => {
            if (!user) {
                res.status(200).json({
                    status: 'success',
                })
            }
            else {
                res.status(200).json({
                    status: 'fail',
                    error: '이미 존재하는 아이디입니다.',
                })
            }
        })
        .catch((error: any) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};