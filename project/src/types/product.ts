import { Types } from 'mongoose';

export interface Product {
    _id: Types.ObjectId;
    name: string,
    userId: string,
    username: string;
    image: string;
    price: number;
    location: string;
    phone: string;
    status: string;
    buyerId: string;
    buyername: string;
    buyerPrice: number;
    wish: number;
    createdAt: Number;
    updatedAt: Number;
}