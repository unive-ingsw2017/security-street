using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SecurityStreet.Server.Services.Autovelox.Models
{
    public static class Defaults
    {
        /// <summary>
        /// Gets the FCM serverkey.
        /// </summary>
        /// <value>
        /// The FCM serverkey.
        /// </value>
        public static string FcmServerkey { get; } = "AAAA-OLNc3U:APA91bGpYZ8mPQNGhXSAWKEi-2bT5nfypHbsE8kMhzxbdyIqJ-_ESHAy3JZgjMNxj4aZNCaiLswT6tyyqeEH8wpVJ3cFSBBV253Iuw1yS53t3FNj0Tf8CHsvL_zmVrRFCAXFDavEm65r";

        /// <summary>
        /// Gets the FCM sender identifier.
        /// </summary>
        /// <value>
        /// The FCM sender identifier.
        /// </value>
        public static string FcmSenderId { get; } = "1068957004661";


        /// <summary>
        /// Gets the FCM notifiction URL.
        /// </summary>
        /// <value>
        /// The FCM notifiction URL.
        /// </value>
        public static string FcmNotifictionUrl { get; } = "https://fcm.googleapis.com/fcm/send";


        /// <summary>
        /// Gets the FCM notify title.
        /// </summary>
        /// <value>
        /// The FCM notify title.
        /// </value>
        public static string FcmNotifyTitle { get; } = "SecurityStreet";


        /// <summary>
        /// Gets the FCM notify body.
        /// </summary>
        /// <value>
        /// The FCM notify body.
        /// </value>
        public static string FcmNotifyBody { get; } = "È stato aggiunto un nuovo autovelox vicino a te";




    }
}
