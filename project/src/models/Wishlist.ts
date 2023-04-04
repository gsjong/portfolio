import { Schema, model } from 'mongoose';
import { Wishlist as WishlistInterface } from '../types/wishlist';

const wishlistSchema = new Schema<WishlistInterface>({
    userId: {
        type: String,
        required: true,
    },
    productId: {
        type: [String],
        default: [],
    },
    createdAt: Date,
    updatedAt: Date,
})

const Wishlist = model<WishlistInterface>('Wishlist', wishlistSchema);

export { Wishlist, wishlistSchema };