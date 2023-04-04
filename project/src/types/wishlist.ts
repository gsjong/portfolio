import { Types } from 'mongoose';

export interface Wishlist {
    _id: Types.ObjectId;
    userId: string;
    productId: string[];
    createdAt: Date;
    updatedAt: Date;
}