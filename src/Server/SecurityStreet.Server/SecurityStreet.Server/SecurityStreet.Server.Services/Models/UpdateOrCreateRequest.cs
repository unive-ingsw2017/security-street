namespace SecurityStreet.Server.Services.Models
{
    public class UpdateOrCreateRequest<Dto>
    {
        /// <summary>
        /// Gets or sets the item.
        /// </summary>
        /// <value>
        /// The item.
        /// </value>
        public Dto Item { get; set; }
    }
}
