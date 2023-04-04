import { Product } from '../models/Product';
import { Product as ProductInterface } from '../types/product';
import { Request, Response } from 'express';
import fs from 'fs';

export const getAllProducts = (req: Request, res: Response) => {
    Product.find()
        .then((products) => {
            res.status(200).json({
                status: 'success',
                data: products,
            })
        })
        .catch((error) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const getOneProduct = (req: Request, res: Response) => {
    Product.find({ "userId": req.body.authUser })
        .then((product) => {
            res.status(200).json({
                status: 'success',
                data: product,
            })
        })
        .catch((error) =>
            res.status(400).json({
                status: 400,
                error: error.message,
            })
        );
};

export const createProduct = (req: Request, res: Response) => {
    if (req.body.authType !== 'seller') {
        return res.status(200).json({
            status: 'fail',
            error: '판매자만 등록할 수 있습니다.',
        })
    }
    req.body.userId = req.body.authUser;
    req.body.status = 'Auction';
    req.body.buyerPrice = req.body.price;
    try {
        const product = new Product(req.body);
        product
            .save()
            .then((newProduct: ProductInterface) =>
                res.status(200).json({
                    status: 'success',
                    data: newProduct,
                })
            )
            .catch((error: any) =>
                res.status(400).json({
                    status: 'fail',
                    error: error.message,
                })
            );
    } catch (error: any) {
        res.status(400).json({
            status: 'fail',
            error: error.message,
        });
    }
};

export const updateProduct = (req: Request, res: Response) => {
    if (req.body.status === 'Purchased') {
        return res.status(200).json({
            status: 'fail',
            error: '경매 종료 함수를 이용해주세요.',
        });
    }
    const body = req.body;
    const id = req.body._id;
    Product.findById(id)
        .then((product) => {
            if (!product) {
                res.status(200).json({
                    status: 'fail',
                    error: 'product not found',
                });
            }
            else if (product.userId !== req.body.authUser) {
                res.status(200).json({
                    status: 'fail',
                    error: '상품을 등록한 판매자만 변경할 수 있습니다.',
                })
            }
            else if (product.status !== 'Auction' && product.buyerId) {
                res.status(200).json({
                    status: 'fail',
                    error: '경매 중이거나 경매가 끝난 상품의 정보를 변경할 수 없습니다.',
                });
            }
            else {
                body.buyerPrice = body.price;
                product.update(body)
                    .then(() => {
                        res.status(200).json({
                            status: 'success',
                            data: product,
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

export const deleteProduct = (req: Request, res: Response) => {
    Product.findById(req.body._id)
        .then((product) => {
            if (!product) {
                res.status(200).json({
                    status: 'fail',
                    error: 'product not found',
                });
            }
            else if (product.userId !== req.body.authUser) {
                res.status(200).json({
                    status: 'fail',
                    error: '상품을 등록한 판매자만 삭제할 수 있습니다.',
                })
            }
            else if (product.status === 'In progress' && product.buyerId) {
                res.status(200).json({
                    status: 'fail',
                    error: '경매 중인 상품은 삭제할 수 없습니다.',
                })
            }
            else {
                if (product.image) {
                    try {
                        fs.unlinkSync(product.image);
                    } catch (error) { }
                }
                product.remove();
                res.status(200).json({
                    status: 'success',
                    message: 'product deleted successfully',
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

export const purchasedProduct = (req: Request, res: Response) => {
    const id = req.body._id;
    Product.findById(id)
        .then((product) => {
            if (!product) {
                res.status(200).json({
                    status: 'fail',
                    error: 'product not found',
                });
            }
            else if (product.userId !== req.body.authUser) {
                res.status(200).json({
                    status: 'fail',
                    error: '상품을 등록한 판매자만 변경할 수 있습니다.',
                })
            }
            else if (product.status !== 'In progress') {
                res.status(200).json({
                    status: 'fail',
                    error: '경매 전이거나 경매가 끝난 상품의 상태를 변경할 수 없습니다.',
                });
            }
            else if (!product.buyerId) {
                product.status = 'Auction';
                product.save();
                res.status(200).json({
                    status: 'nobody',
                    error: '아무도 구매하지 않아 상품이 유찰되었습니다.',
                });
            }
            else {
                product.status = 'Purchased';
                product.save();
                res.status(200).json({
                    status: 'success',
                    data: product,
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

export const buyProduct = (req: Request, res: Response) => {
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
    Product.findById(id)
        .then((product) => {
            if (!product) {
                res.status(200).json({
                    status: 'fail',
                    error: 'product not found',
                });
            }
            else if (req.body.name || req.body.userId || req.body.username || req.body.image || req.body.location || req.body.phone || req.body.status || req.body.wish || req.body.price) {
                res.status(200).json({
                    status: 'fail',
                    error: '가격 정보만 변경할 수 있습니다.',
                })
            }
            else if (req.body.buyerPrice <= product.buyerPrice) {
                res.status(200).json({
                    status: 'fail',
                    error: '현재 경매가보다 높은 금액만 제시할 수 있습니다.',
                })
            }
            else if (product.status !== 'In progress') {
                res.status(200).json({
                    status: 'fail',
                    error: '경매 전이거나 경매가 끝난 상품입니다.',
                });
            }
            else {
                product.buyerId = req.body.authUser;
                product.buyername = req.body.buyername;
                product.buyerPrice = req.body.buyerPrice;
                product.save()
                    .then(() => {
                        res.status(200).json({
                            status: 'success',
                            data: product,
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

export const getBuyerProduct = (req: Request, res: Response) => {
    Product.find({ "buyerId": req.body.authUser, "status": "Purchased" })
        .then((product) => {
            res.status(200).json({
                status: 'success',
                data: product,
            })
        })
        .catch((error) =>
            res.status(400).json({
                status: 400,
                error: error.message,
            })
        );
};

export const searchProduct = (req: Request, res: Response) => {
    const body = req.body;
    let filter: any = {};
    if (body.username) {
        filter.username = body.username;
    }
    if (body.name) {
        filter.name = { $regex: new RegExp('.*' + body.name + '*'), $options: 'i' }
    }
    if (body.minPrice && body.maxPrice) {
        filter.buyerPrice = { $gte: body.minPrice, $lte: body.maxPrice };
    }
    else if (body.minPrice) {
        filter.buyerPrice = { $gte: body.minPrice };
    }
    else if (body.maxPrice) {
        filter.buyerPrice = { $lte: body.maxPrice };
    }
    Product.find(filter)
        .then((products) =>
            res.status(200).json({
                status: 'success',
                data: products,
            })
        )
        .catch((error) =>
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        );
};

export const uploadImage = (req: Request, res: Response) => {
    const id: string = req.params.id;
    Product.findById(id)
        .then((product) => {
            if (!product) {
                res.status(200).json({
                    status: 'fail',
                    error: 'product not found',
                })
            }
            else {
                if (!req.file) {
                    res.status(200).json({
                        status: 'fail',
                        error: 'file not found',
                    })
                }
                else if (product.status !== 'Auction' && product.buyerId) {
                    try {
                        fs.unlinkSync(req.file.path);
                    } catch (error) { }
                    res.status(200).json({
                        status: 'fail',
                        error: '경매 중이거나 경매가 끝난 상품의 정보를 변경할 수 없습니다.',
                    });
                }
                else {
                    if (product.image) {
                        try {
                            fs.unlinkSync(product.image);
                        } catch (error) { }
                    }
                    product.image = req.file.path;
                    product.save();
                    res.status(200).json({
                        status: 'success',
                        data: product,
                    })
                }
            }
        })
        .catch((error) => {
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        })
}

export const deleteImage = (req: Request, res: Response) => {
    const id: string = req.params.id;
    Product.findById(id)
        .then((product) => {
            if (!product) {
                res.status(200).json({
                    status: 'fail',
                    error: 'product not found',
                })
            }
            else if (product.status !== 'Auction' && product.buyerId) {
                res.status(200).json({
                    status: 'fail',
                    error: '경매 중이거나 경매가 끝난 상품의 정보를 변경할 수 없습니다.',
                });
            }
            else {
                if (!product.image) {
                    return res.status(200).json({
                        stauts: 'fail',
                        error: '등록된 상품 이미지가 없습니다.'
                    })
                }
                try {
                    fs.unlinkSync(product.image);
                } catch (error) { }
                product.image = "";
                product.save();
                res.status(200).json({
                    status: 'success',
                    data: product,
                })
            }
        })
        .catch((error) => {
            res.status(400).json({
                status: 'fail',
                error: error.message,
            })
        })
}