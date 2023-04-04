import { Wishlist } from '../models/Wishlist';
import { Request, Response } from 'express';
import { Product } from '../models/Product';

export const getAllWishlists = (req: Request, res: Response) => {
    Wishlist.find()
        .then((wishlists) =>
            res.status(200).json({
                status: 'success',
                data: wishlists,
            })
        )
        .catch((error) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const getOneWishlist = (req: Request, res: Response) => {
    Wishlist.findOne({ "userId": req.body.authUser })
        .then((wishlist) => {
            if (!wishlist) {
                return res.status(200).json({
                    status: 'fail',
                    error: 'wishlist not found',
                });
            }
            else {
                var list: any[] = [];
                wishlist.productId.forEach(productId => {
                    Product.findById(productId)
                        .then((product) => {
                            if (!product) {
                                wishlist.productId.splice(wishlist.productId.indexOf(productId), 1);
                            }
                            else {
                                list.push(product);
                            }
                        })
                })
                setTimeout(() => {
                    wishlist.save();
                    res.status(200).json({
                        status: 'success',
                        data: list,
                    });
                }, 200);
            }
        })
        .catch((error) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const addWishlist = (req: Request, res: Response) => {
    if (req.body.authType === 'guest') {
        return res.status(200).json({
            status: 'login',
            error: '구매자만 이용할 수 있습니다.',
        });
    }
    else if (req.body.authType === 'seller' || req.body.authType === 'admin') {
        return res.status(200).json({
            status: 'fail',
            error: '구매자만 이용할 수 있습니다.',
        });
    }
    const id = req.body._id;
    Wishlist.findOne({ "userId": req.body.authUser })
        .then((wishlist) => {
            if (!wishlist) {
                res.status(200).json({
                    status: 'fail',
                    error: 'wishlist not found',
                });
            }
            else if (wishlist.productId.includes(id)) {
                res.status(200).json({
                    status: 'fail',
                    error: '이미 찜한 상품입니다.'
                })
            }
            else {
                wishlist.productId.push(id);
                wishlist.save();
                Product.findById(id)
                    .then((product) => {
                        if (product) {
                            product.wish += 1;
                            product.save();
                        }
                    })
                res.status(200).json({
                    status: 'success',
                    data: wishlist,
                });
            }
        })
        .catch((error) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const deleteWishlist = (req: Request, res: Response) => {
    if (req.body.authType === 'guest') {
        return res.status(200).json({
            status: 'login',
            error: '구매자만 이용할 수 있습니다.',
        });
    }
    else if (req.body.authType === 'seller' || req.body.authType === 'admin') {
        return res.status(200).json({
            status: 'fail',
            error: '구매자만 이용할 수 있습니다.',
        });
    }
    const id = req.body._id;
    Wishlist.findOne({ "userId": req.body.authUser })
        .then((wishlist) => {
            if (!wishlist) {
                res.status(200).json({
                    status: 'fail',
                    error: 'wishlist not found',
                });
            }
            else if (!wishlist.productId.includes(id)) {
                res.status(200).json({
                    status: 'fail',
                    error: '찜하지 않은 상품입니다.'
                })
            }
            else {
                wishlist.productId.splice(wishlist.productId.indexOf(id), 1);
                wishlist.save();
                Product.findById(id)
                    .then((product) => {
                        if (product) {
                            product.wish -= 1;
                            product.save();
                        }
                    })
                res.status(200).json({
                    status: 'success',
                    data: wishlist,
                });
            }
        })
        .catch((error) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};