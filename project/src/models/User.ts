import { Schema, model } from 'mongoose';
import { User as UserInterface } from '../types/user';

const userSchema = new Schema<UserInterface>({
    username: {
        type: String,
        required: true,
    },
    password: {
        type: String,
        required: true,
    },
    type: {
        type: String,
        required: true,
    },
    createdAt: Date,
    updatedAt: Date,
})

const User = model<UserInterface>('User', userSchema);

export { User, userSchema };