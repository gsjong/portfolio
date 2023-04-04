import express from 'express';
import mongoose from 'mongoose';
import * as dotenv from 'dotenv';
import cookieParser from 'cookie-parser';
import fs from 'fs';

dotenv.config();

try {
    fs.readdirSync('uploads');
}
catch (error) {
    fs.mkdirSync('uploads');
}

const app = express();

app.use(express.json());
app.use(cookieParser());
app.use('/uploads', express.static('uploads'));
app.use('/etc', express.static('etc'));

mongoose
    .connect(process.env.MONGO_URI as string, {})
    .then(() => console.log('몽고디비 연결완료'))
    .catch((error) => console.log(error));

app.listen(8000, () => {
    console.log('8000번 포트 대기중...');
});

import ProductRouter from './routes/Product';
import UserRouter from './routes/User';
import WishlistRouter from './routes/Wishlist';

app.use('/product', ProductRouter);
app.use('/user', UserRouter);
app.use('/wishlist', WishlistRouter);

import { auth } from './middlewares/auth';

app.get('/', (req, res) => {
    res.sendFile(__dirname + "/front/index.html");
});

app.get('/style', (req, res) => {
    res.sendFile(__dirname + "/front/style.css");
});

app.get('/login', auth, (req, res) => {
    if (req.body.authType === "guest") {
        res.sendFile(__dirname + "/front/login.html");
    }
    else {
        res.redirect("/");
    }
});

app.get('/register', auth, (req, res) => {
    if (req.body.authType === "guest") {
        res.sendFile(__dirname + "/front/register.html");
    }
    else {
        res.redirect("/");
    }
});

app.get('/mypage', auth, (req, res) => {
    if (req.body.authType === "admin") {
        res.sendFile(__dirname + "/front/admin.html");
    }
    else if (req.body.authType === "seller") {
        res.sendFile(__dirname + "/front/seller.html");
    }
    else if (req.body.authType === "buyer") {
        res.sendFile(__dirname + "/front/buyer.html");
    }
    else {
        res.redirect("/login");
    }
});

app.get('/auction', auth, (req, res) => {
    if (req.body.authType === "seller") {
        res.sendFile(__dirname + "/front/auction.html");
    }
    else if (req.body.authType === "admin" || req.body.authType === "buyer") {
        console.log("판매자만 이용할 수 있습니다.");
    }
    else {
        res.redirect("/login");
    }
});

app.get('/wish', auth, (req, res) => {
    if (req.body.authType === "buyer") {
        res.sendFile(__dirname + "/front/wish.html");
    }
    else if (req.body.authType === "admin" || req.body.authType === "seller") {
        console.log("구매자만 이용할 수 있습니다.");
    }
    else {
        res.redirect("/login");
    }
});

app.get('/change', auth, (req, res) => {
    if (req.body.authType === "guest") {
        res.redirect("/login");
    }
    else {
        res.sendFile(__dirname + "/front/change.html");
    }
});

import { Product } from './models/Product';

function autoProduct() {
    Product.find({ "status": "In progress" })
        .then((products) => {
            products.forEach((product) => {
                if (Number(product.updatedAt) - Date.now() < 0) {
                    if (!product.buyerId) {
                        product.status = 'Auction';
                        product.save();
                    }
                    else {
                        product.status = 'Purchased';
                        product.save();
                    }
                }
            })
        })
        .catch((error) => {
            console.log(error)
        });
}

setInterval(autoProduct, 1000 * 60 * 60);