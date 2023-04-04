import { Schema, model } from 'mongoose';
import { Product as ProductInterface } from '../types/product';

const productSchema = new Schema<ProductInterface>({
    name: {
        type: String,
        required: true,
    },
    userId: {
        type: String,
        required: true,
    },
    username: {
        type: String,
        required: true,
    },
    image: {
        type: String,
    },
    price: {
        type: Number,
        default: 0,
    },
    location: {
        type: String,
        required: true,
    },
    phone: {
        type: String,
        required: true,
    },
    status: {
        type: String,
    },
    buyerId: {
        type: String,
    },
    buyername: {
        type: String,
        default: "없음",
    },
    buyerPrice: {
        type: Number,
    },
    wish: {
        type: Number,
        default: 0,
    },
    createdAt: Number,
    updatedAt: Number,
})

const Product = model<ProductInterface>('Product', productSchema);

export { Product, productSchema };