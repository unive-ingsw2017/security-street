using SecurityStreet.Server.Models.Dto;
using ServiceStack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SecurityStreet.Server.Services.Autovelox.Models
{
    [Route("/autovelox", "POST")]
    public class UpdateAutoveloxRequest : IReturn<AutoveloxDto>
    {
        /// <summary>
        /// Gets or sets the item.
        /// </summary>
        /// <value>
        /// The item.
        /// </value>
        public AutoveloxDto Item { get; set; }
    }
}
