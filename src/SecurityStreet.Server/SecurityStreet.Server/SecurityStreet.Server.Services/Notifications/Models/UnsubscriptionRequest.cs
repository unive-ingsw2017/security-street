using SecurityStreet.Server.Models.Dto;
using ServiceStack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SecurityStreet.Server.Services.Autovelox.Models
{
    [Route("/notifications")]
    public class UnsubscriptionRequest : IReturn<bool>
    {
        /// <summary>
        /// Gets or sets the client token.
        /// </summary>
        /// <value>
        /// The client token.
        /// </value>
        public string ClientToken { get; set; }
    }
}
